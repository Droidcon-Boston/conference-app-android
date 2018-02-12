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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.views.agenda.AgendaFragment;
import com.mentalmachines.droidcon_boston.views.myschedule.MyScheduleFragment;
import com.mentalmachines.droidcon_boston.views.social.SocialFragment;
import com.mentalmachines.droidcon_boston.views.speakers.SpeakersFragment;

public class MainActivity extends AppCompatActivity {

  ActionBarDrawerToggle actionBarDrawerToggle;

  DrawerLayout androidDrawerLayout;

  @BindView(R.id.navView)
  NavigationView navigationView;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  final FragmentManager fragmentManager = getSupportFragmentManager();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    // Setup ButterKnife
    ButterKnife.bind(this);

    // Setup Navigation Drawer
    initNavDrawerToggle();

    // Initially load Agenda Screen
    replaceFragment(new AgendaFragment(), "Agenda");
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
        case R.id.nav_my_schedule:
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
          break;
        case R.id.nav_faq:
          replaceFragment(new FAQFragment(), "FAQ");
          break;
        case R.id.nav_about_us:

          replaceFragment(new AboutUsFragment(), "About Us");
          break;
        case R.id.nav_coc:
          replaceFragment(new CocFragment(), "Code Of Conduct");
          break;
      }
      return true;
    });

    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

  public void faqClick(View v) {
    final Intent tnt = new Intent(Intent.ACTION_VIEW);
    tnt.setData(Uri.parse((String) v.getTag()));
    startActivity(tnt);
  }
}
