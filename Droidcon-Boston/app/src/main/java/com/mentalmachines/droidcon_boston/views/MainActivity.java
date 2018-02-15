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

    replaceFragment(new AgendaFragment(), getString(R.string.str_agenda));
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
      item.setChecked(!item.isChecked());

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
          replaceFragment(new AgendaFragment(), getString(R.string.str_agenda));
          break;
        case R.id.nav_faq:
          replaceFragment(new FAQFragment(), getString(R.string.str_faq));
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
