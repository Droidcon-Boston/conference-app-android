package com.mentalmachines.droidcon_boston.views;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.views.base.BaseActivity;
import com.mentalmachines.droidcon_boston.views.base.MaterialActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends MaterialActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // define your fragments here
        final Fragment agendaFragment = new AgendaFragment();
        final Fragment chatFragment = new ChatFragment();
        final Fragment tweetsFragment = new TweetsFragment();

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.action_agenda:
                                fragmentTransaction.replace(R.id.fragment_container, agendaFragment).commit();
                                return true;
                            case R.id.action_chat:
                                fragmentTransaction.replace(R.id.fragment_container, chatFragment).commit();
                                return true;
                            case R.id.action_twitter:
                                fragmentTransaction.replace(R.id.fragment_container, tweetsFragment).commit();
                                return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
