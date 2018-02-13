package com.mentalmachines.droidcon_boston.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.views.agenda.AgendaFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "csLPIPIQ6AoWyhzCSHlK2lOen";

    private static final String TWITTER_SECRET = "p3c45qpNvIOQiTZi6iK9Cffb3xRH4X7SThT4EfVo7fIu42SNWD";

    final FragmentManager fragmentManager = getSupportFragmentManager();

    DrawerLayout androidDrawerLayout;

    ActionBarDrawerToggle actionBarDrawerToggle;

    NavigationView navigationView;

    Toolbar toolbar;


    ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        initNavDrawerToggle();

        replaceFragment(new AgendaFragment(), "Agenda");
    }


    private void initNavDrawerToggle() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        androidDrawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, androidDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        androidDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(item -> {

            //Checking if the item is in checked state or not, if not make it in checked state
            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }

            //Closing drawer on item click
            androidDrawerLayout.closeDrawers();

            switch (item.getItemId()) {
                // Respond to the action bar's Up/Home button
                case android.R.id.home:
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    } else if (fragmentManager.getBackStackEntryCount() == 1) {
                        // to avoid looping below on initScreen
                        super.onBackPressed();
                        finish();
                    }
                    break;
                case R.id.nav_agenda:
                    replaceFragment(new AgendaFragment(), "Agenda");
                    break;
                /* case R.id.nav_my_schedule:
                    replaceFragment(new MyScheduleFragment(), "My Schedule");
                    break;
                case R.id.nav_speakers:
                    replaceFragment(new SpeakersFragment(), "Speakers");
                    break;
                case R.id.nav_social:
                    replaceFragment(new SocialFragment(), "Social");
                    break;
                case R.id.nav_settings:
                    replaceFragment(new SettingsFragment(), "Settings");
                    break;*/
                case R.id.nav_faq:
                    replaceFragment(new FAQFragment(), "FAQ");
                    break;
                /*
                case R.id.nav_about_us:
                    // replaceFragment(new AboutUsFragment(), "About Us");
                    break;
                case R.id.nav_coc:
                    // replaceFragment(new CocFragment(), "Code Of Conduct");
                    break;*/
            }
            return true;
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // This is required to make the drawer toggle work
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment, String title) {
        updateToolbarTitle(title);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void updateToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

//    Code below is commented for a future PR regarding the Social Screen
//
//    class DrawerItemClickListener implements ListView.OnItemClickListener {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            if (position < 3) {
//                ((NavigationAdapter) parent.getAdapter()).setSelectedIndex(position);
//            } //others are contact links
//            Uri data = null;
//            switch (position) {
//                case 0: //agenda
//                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new AgendaFragment())
//                            .commit();
//                    break;
//                case 1: //tweet
//                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new TweetsFragment())
//                            .commit();
//                    break;
//                case 2: // faq
//                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new FAQFragment()).commit();
//                    break;
//                case 3: //contact, us
//                    data = Uri.parse(getString(R.string.contact_link));
//                    break;
//                case 4: // contact, facebook
//                    data = Uri.parse(getString(R.string.facebook_link));
//                    break;
//                case 5: // contact, instagram
//                    data = Uri.parse(getString(R.string.instagram_link));
//                    break;
//                case 6: // contact, linkedin
//                    data = Uri.parse(getString(R.string.linkedin_link));
//                    break;
//                case 7: // contact, twitter
//                    data = Uri.parse(getString(R.string.twitter_link));
//                    break;
//            }
//            if (data == null) {
//                fragmentManager.executePendingTransactions();
//            } else {
//                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
//                        .setToolbarColor(getResources().getColor(R.color.colorPrimary))
//                        .build();
//                customTabsIntent.launchUrl(view.getContext(), data);
//            }
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        }
//    } //end click listener

    public void faqClick(View v) {
        final Intent tnt = new Intent(Intent.ACTION_VIEW);
        tnt.setData(Uri.parse((String) v.getTag()));
        startActivity(tnt);
    }
}
