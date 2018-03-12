package com.mentalmachines.droidcon_boston.views

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.views.agenda.AgendaFragment
import com.mentalmachines.droidcon_boston.views.social.SocialFragment
import com.mentalmachines.droidcon_boston.views.speaker.SpeakerFragment
import com.mentalmachines.droidcon_boston.views.volunteer.VolunteerFragment
import kotlinx.android.synthetic.main.main_activity.drawer_layout
import kotlinx.android.synthetic.main.main_activity.navView
import kotlinx.android.synthetic.main.main_activity.toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private var lastFragmentTitleSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initNavDrawerToggle()

        replaceFragment(getString(R.string.str_agenda))
        navView.setCheckedItem(R.id.nav_agenda)
    }


    override fun onBackPressed() {
        // If drawer is open
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            // close the drawer
            drawer_layout.closeDrawer(Gravity.LEFT)
        } else {
            super.onBackPressed()
        }
    }


    private fun initNavDrawerToggle() {

        setSupportActionBar(toolbar)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout,
                R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(actionBarDrawerToggle)

        navView.setNavigationItemSelectedListener { item ->

            navView.setCheckedItem(item.itemId)

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
        if (title == lastFragmentTitleSelected) {
            // Fragment currently selected, no action.
            return
        }

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
            supportFragmentManager.beginTransaction()
                    // detach the fragment that is currently visible
                    .detach(supportFragmentManager.findFragmentById(R.id.fragment_container))
                    // attach the fragment found as per the tag
                    .attach(fragment)
                    // commit fragment transaction
                    .commit()
        }

        lastFragmentTitleSelected = title
    }

    private fun updateToolbarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar?.title = title
        }
    }
}
