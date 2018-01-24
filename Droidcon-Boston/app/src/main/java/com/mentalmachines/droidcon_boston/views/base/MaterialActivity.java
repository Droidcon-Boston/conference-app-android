package com.mentalmachines.droidcon_boston.views.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import com.mentalmachines.droidcon_boston.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class MaterialActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

    }

    @Override
    public int getLayout() {
        return R.layout.main_activity;
    }
}
