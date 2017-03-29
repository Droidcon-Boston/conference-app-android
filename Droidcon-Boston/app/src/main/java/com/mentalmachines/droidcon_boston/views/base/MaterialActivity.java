package com.mentalmachines.droidcon_boston.views.base;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jinn on 3/11/17.
 */

public abstract class MaterialActivity extends BaseActivity {
    // @BindView(R.id.fab) FloatingActionButton fab;
    // @BindView(R.id.recycler) RecyclerView recycler;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public int getLayout() {
        return R.layout.main_activity;
    }
}
