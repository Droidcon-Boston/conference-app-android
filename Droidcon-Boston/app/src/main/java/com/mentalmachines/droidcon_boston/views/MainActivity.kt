package com.mentalmachines.droidcon_boston.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.R.id
import com.mentalmachines.droidcon_boston.R.string
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.utils.ServiceLocator
import com.mentalmachines.droidcon_boston.views.agenda.AgendaFragment
import com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment
import com.mentalmachines.droidcon_boston.views.social.SocialFragment
import com.mentalmachines.droidcon_boston.views.speaker.SpeakerFragment
import com.mentalmachines.droidcon_boston.views.volunteer.VolunteerFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initNavDrawerToggle()

        initFragmentsFromIntent(intent)
    }

    private fun initFragmentsFromIntent(initialIntent: Intent) {
        replaceFragment(getString(string.str_agenda))

        val sessionDetails = initialIntent.extras?.getString(EXTRA_SESSION_DETAILS)
        if (!TextUtils.isEmpty(sessionDetails)) {
            AgendaDetailFragment.addDetailFragmentToStack(
                supportFragmentManager,
                ServiceLocator.gson.fromJson(sessionDetails, ScheduleRow::class.java)
            )
            updateSelectedNavItem(supportFragmentManager)
        } else {
            navView.setCheckedItem(id.nav_agenda)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            initFragmentsFromIntent(it)
        }
    }


    override fun onBackPressed() {
        // If drawer is open
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            // close the drawer
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

            val manager = supportFragmentManager
            if (manager.backStackEntryCount == 0) {
                // special handling where user clicks on back button in a detail fragment
                updateSelectedNavItem(manager)
            }
        }
    }

    private fun updateSelectedNavItem(manager: FragmentManager) {
        val currentFragment = manager.findFragmentById(id.fragment_container)
        if (currentFragment is AgendaFragment) {
            if (currentFragment.isMyAgenda()) {
                checkNavMenuItem(getString(string.str_my_schedule))
            } else {
                checkNavMenuItem(getString(string.str_agenda))
            }
        } else if (currentFragment is SpeakerFragment) {
            checkNavMenuItem(getString(string.str_speakers))
        }
    }

    private fun checkNavMenuItem(title: String) {
        processMenuItems({ item -> item.title == title },
            { item -> item.setChecked(true).isChecked },
            processAll = true)
    }

    private fun isNavItemChecked(title: String): Boolean {
        return processMenuItems({ item -> item.title == title }, { item -> item.isChecked })
    }

    fun uncheckAllMenuItems() {
        processMenuItems({ true }, { item -> item.setChecked(false).isChecked }, true)
    }

    private fun processMenuItems(
        titleMatcher: (MenuItem) -> Boolean,
        matchFunc: (MenuItem) -> Boolean,
        processAll: Boolean = false
    ): Boolean {
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
            } else {
                item.isChecked = false
            }
        }
        return processAll
    }


    private fun initNavDrawerToggle() {

        setSupportActionBar(toolbar)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawer_layout.addDrawerListener(actionBarDrawerToggle)

        navView.setNavigationItemSelectedListener { item ->

            //Closing drawer on item click
            drawer_layout.closeDrawers()

            when (item.itemId) {
                // Respond to the action bar's Up/Home button
                android.R.id.home -> if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else if (supportFragmentManager?.backStackEntryCount == 1) {
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
                R.id.nav_login_logout -> {
                    if (user != null) {
                        logout()
                    } else {
                        login()
                    }
                }
            }

            if (item.itemId != R.id.nav_login_logout) {
                navView.setCheckedItem(item.itemId)
            } else {
                updateSelectedNavItem(supportFragmentManager)
            }

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
        var fragment: androidx.fragment.app.Fragment? =
            supportFragmentManager.findFragmentByTag(title)

        if (fragment == null) {
            // Initialize the fragment based on tag
            when (title) {
                resources.getString(R.string.str_agenda) -> fragment = AgendaFragment.newInstance()
                resources.getString(R.string.str_my_schedule) -> fragment =
                        AgendaFragment.newInstanceMySchedule()
                resources.getString(R.string.str_faq) -> fragment = FAQFragment()
                resources.getString(R.string.str_social) -> fragment = SocialFragment()
                resources.getString(R.string.str_coc) -> fragment = CocFragment()
                resources.getString(R.string.str_about_us) -> fragment = AboutFragment()
                resources.getString(R.string.str_speakers) -> fragment = SpeakerFragment()
                resources.getString(R.string.str_volunteers) -> fragment = VolunteerFragment()
            }
            // Add fragment with tag
            fragment?.let {
                supportFragmentManager?.beginTransaction()
                    // replace in container
                    ?.replace(R.id.fragment_container, it, title)
                    // commit fragment transaction
                    ?.commit()
            }
        } else {

            // For Agenda and My Schedule Screen, which add more fragments to backstack.
            // Remove all fragment except the last one when navigating via the nav drawer.
            when (title) {
                resources.getString(R.string.str_agenda) -> popUntilLastFragment()
                resources.getString(R.string.str_my_schedule) -> popUntilLastFragment()
            }

            val fragmentInContainer =
                supportFragmentManager?.findFragmentById(R.id.fragment_container)
            fragmentInContainer?.let {
                supportFragmentManager?.beginTransaction()
                    // detach the fragment that is currently visible
                    ?.detach(it)
                    // attach the fragment found as per the tag
                    ?.attach(it)
                    // commit fragment transaction
                    ?.commit()
            }
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

    fun login() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)
                .build(),
            RC_SIGN_IN)
    }

    fun logout() {
        AuthUI.getInstance()
            .signOut(this)
        user = null
        navView.menu.findItem(R.id.nav_login_logout).title = getString(R.string.str_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                user = FirebaseAuth.getInstance().currentUser
                navView.menu.findItem(R.id.nav_login_logout).title = getString(R.string.str_logout)
            } else {
                response?.let {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.str_title_error)
                        .setMessage(it.error?.message)
                        .show()
                }
            }
        }
    }

    companion object {
        private const val EXTRA_SESSIONID = "MainActivity.EXTRA_SESSIONID"
        private const val EXTRA_SESSION_DETAILS = "MainActivity.EXTRA_SESSION_DETAILS"

        private const val RC_SIGN_IN = 1

        fun getSessionDetailIntent(
            context: Context,
            sessionId: String,
            sessionDetail: String
        ): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_SESSIONID, sessionId)
                putExtra(EXTRA_SESSION_DETAILS, sessionDetail)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }
    }
}
