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
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.views.agenda.AgendaFragment;
import com.mentalmachines.droidcon_boston.views.social.SocialFragment;

public class MainActivity extends AppCompatActivity {

  final FragmentManager fragmentManager = getSupportFragmentManager();

  DrawerLayout androidDrawerLayout;

  ActionBarDrawerToggle actionBarDrawerToggle;

  NavigationView navigationView;

  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    initNavDrawerToggle();

    replaceFragment(getString(R.string.str_agenda));
    navigationView.setCheckedItem(R.id.nav_agenda);
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

      navigationView.setCheckedItem(item.getItemId());

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
          replaceFragment(getString(R.string.str_agenda));
          break;
        case R.id.nav_faq:
          replaceFragment(getString(R.string.str_faq));
          break;
        case R.id.nav_social:
          replaceFragment(getString(R.string.str_social));
          break;
      }
      return true;
    });

    if (getSupportActionBar() != null) {
      getSupportActionBar().setHomeButtonEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
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

  private void replaceFragment(String title) {
    updateToolbarTitle(title);

    // Get the fragment by tag
    Fragment fragment = fragmentManager.findFragmentByTag(title);

    if (fragment == null) {
      // Initialize the fragment based on tag
      if (title.equals(getResources().getString(R.string.str_agenda))) {
        fragment = new AgendaFragment();
      } else if (title.equals(getResources().getString(R.string.str_faq))) {
        fragment = new FAQFragment();
      } else if (title.equals(getResources().getString(R.string.str_social))) {
        fragment = new SocialFragment();
      }
      // Add fragment with tag
      fragmentManager.beginTransaction().add(R.id.fragment_container, fragment, title).commit();
    } else {
      fragmentManager.beginTransaction()
          // detach the fragment that is currently visible
          .detach(fragmentManager.findFragmentById(R.id.fragment_container))
          // attach the fragment found as per the tag
          .attach(fragment)
          // commit fragment transaction
          .commit();
    }
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
