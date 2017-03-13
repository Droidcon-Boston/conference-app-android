package com.mentalmachines.droidcon_boston.views.base;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mentalmachines.droidcon_boston.R;

/**
 * Created by jinn on 3/11/17.
 */

public abstract class MaterialActivity extends BaseActivity {
    FloatingActionButton fab;
    RecyclerView recycler;
    DrawerLayout navigationDrawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    ListView drawerList;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Set the drawer toggle as the DrawerListener
        navigationDrawer.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.getResources().getStringArray(R.array.navigation_drawer)));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    public int getLayout() {
        return 0;
    }
}
