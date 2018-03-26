package com.mentalmachines.droidcon_boston.views

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.MenuItem
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.utils.ServiceLocator
import com.mentalmachines.droidcon_boston.views.agenda.AgendaFragment
import com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment
import com.mentalmachines.droidcon_boston.views.social.SocialFragment
import com.mentalmachines.droidcon_boston.views.speaker.SpeakerFragment
import com.mentalmachines.droidcon_boston.views.volunteer.VolunteerFragment
import kotlinx.android.synthetic.main.main_activity.drawer_layout
import kotlinx.android.synthetic.main.main_activity.navView
import kotlinx.android.synthetic.main.main_activity.toolbar


class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initNavDrawerToggle()

        replaceFragment(getString(R.string.str_agenda))

        val sessionDetails = intent.extras?.getString(EXTRA_SESSION_DETAILS)
        if (!TextUtils.isEmpty(sessionDetails)) {
            AgendaDetailFragment.addDetailFragmentToStack(supportFragmentManager,
                    ServiceLocator.gson.fromJson(sessionDetails, ScheduleRow::class.java))
        } else {
            navView.setCheckedItem(R.id.nav_agenda)
        }
    }


    override fun onBackPressed() {
        // If drawer is open
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            // close the drawer
            drawer_layout.closeDrawer(Gravity.START)
        } else {
            super.onBackPressed()

            val manager = supportFragmentManager
            if (manager.backStackEntryCount == 0) {
                // special handling where user clicks on back button in a detail fragment
                val currentFragment = manager.findFragmentById(R.id.fragment_container)
                if (currentFragment is AgendaFragment) {
                    if (currentFragment.isMyAgenda()) {
                        checkNavMenuItem(getString(R.string.str_my_schedule))
                    } else {
                        checkNavMenuItem(getString(R.string.str_agenda))
                    }
                } else if (currentFragment is SpeakerFragment) {
                    checkNavMenuItem(getString(R.string.str_speakers))
                }
            }
        }
    }

    private fun checkNavMenuItem(title: String) {
        processMenuItems({ item -> item.title == title }, { item -> item.setChecked(true).isChecked })
    }

    private fun isNavItemChecked(title: String): Boolean {
        return processMenuItems({ item -> item.title == title }, { item -> item.isChecked })
    }

    fun uncheckAllMenuItems() {
        processMenuItems({ _ -> true }, { item -> item.setChecked(false).isChecked }, true)
    }

    private fun processMenuItems(titleMatcher: (MenuItem) -> Boolean,
                                 matchFunc: (MenuItem) -> Boolean,
                                 processAll: Boolean = false): Boolean {
        val menu = navView.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.hasSubMenu()) {
                val subMenu = item.subMenu
                for (j in 0 until subMenu.size()) {
                    val subMenuItem = subMenu.getItem(j)

                    if (titleMatcher(subMenuItem)) {
                        val result = matchFunc(subMenuItem)
                        if (!processAll) {
                            return result
                        }
                    }
                }
            } else if (titleMatcher(item)) {
                val result = matchFunc(item)
                if (!processAll) {
                    return result
                }
            }
        }
        return false
    }


    private fun initNavDrawerToggle() {

        setSupportActionBar(toolbar)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout,
                R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(actionBarDrawerToggle)

        navView.setNavigationItemSelectedListener { item ->

            //Closing drawer on item click
            drawer_layout.closeDrawers()

            when (item.itemId) {
            // Respond to the action bar's Up/Home button
                android.R.id.home -> if (fragmentManager.backStackEntryCount > 0) {
                    fragmentManager.popBackStack()
                } else if (fragmentManager.backStackEntryCount == 1) {
                    // to avoid looping below on initScreen
                    super.onBackPressed()
                    finish()
                }
                R.id.nav_agenda -> replaceFragment(getString(R.string.str_agenda))
                R.id.nav_my_schedule -> replaceFragment(getString(R.string.str_my_schedule))
                R.id.nav_faq -> replaceFragment(getString(R.string.str_faq))
                R.id.nav_social -> replaceFragment(getString(R.string.str_social))
                R.id.nav_coc -> replaceFragment(getString(R.string.str_coc))
                R.id.nav_about -> replaceFragment(getString(R.string.str_about_us))
                R.id.nav_speakers -> replaceFragment(getString(R.string.str_speakers))
                R.id.nav_volunteers -> replaceFragment(getString(R.string.str_volunteers))
            }

            navView.setCheckedItem(item.itemId)

            true
        }

        if (supportActionBar != null) {
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // This is required to make the drawer toggle work
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun replaceFragment(title: String) {
        if (isNavItemChecked(title)) {
            // Fragment currently selected, no action.
            return
        }

        checkNavMenuItem(title)

        updateToolbarTitle(title)

        // Get the fragment by tag
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(title)

        if (fragment == null) {
            // Initialize the fragment based on tag
            when (title) {
                resources.getString(R.string.str_agenda) -> fragment = AgendaFragment.newInstance()
                resources.getString(R.string.str_my_schedule) -> fragment = AgendaFragment.newInstanceMySchedule()
                resources.getString(R.string.str_faq) -> fragment = FAQFragment()
                resources.getString(R.string.str_social) -> fragment = SocialFragment()
                resources.getString(R.string.str_coc) -> fragment = CocFragment()
                resources.getString(R.string.str_about_us) -> fragment = AboutFragment()
                resources.getString(R.string.str_speakers) -> fragment = SpeakerFragment()
                resources.getString(R.string.str_volunteers) -> fragment = VolunteerFragment()
            }
            // Add fragment with tag
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, title).commit()
        } else {

            // For Agenda and My Schedule Screen, which add more fragments to backstack.
            // Remove all fragment except the last one when navigating via the nav drawer.
            when (title) {
                resources.getString(R.string.str_agenda) ->
                    popUntilLastFragment()
                resources.getString(R.string.str_my_schedule) ->
                    popUntilLastFragment()
            }

            supportFragmentManager.beginTransaction()
                    // detach the fragment that is currently visible
                    .detach(supportFragmentManager.findFragmentById(R.id.fragment_container))
                    // attach the fragment found as per the tag
                    .attach(fragment)
                    // commit fragment transaction
                    .commit()
        }
    }

    private fun popUntilLastFragment() {
        for (i in 0..supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun updateToolbarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar?.title = title
        }
    }

    companion object {
        private const val EXTRA_SESSIONID = "MainActivity.EXTRA_SESSIONID"
        private const val EXTRA_SESSION_DETAILS = "MainActivity.EXTRA_SESSION_DETAILS"

        fun getSessionDetailIntent(context: Context, sessionId: String, sessionDetail: String): Intent {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_SESSIONID, sessionId)
                putExtra(EXTRA_SESSION_DETAILS, sessionDetail)
            }
            return intent
        }
    }
}
