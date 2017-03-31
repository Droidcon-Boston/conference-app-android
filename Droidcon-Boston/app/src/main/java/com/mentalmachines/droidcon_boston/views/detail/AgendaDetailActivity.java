package com.mentalmachines.droidcon_boston.views.detail;

import android.os.Bundle;
import android.view.MenuItem;

import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.views.base.MaterialActivity;

/**
 * Created by jinn on 3/31/17.
 */

public class AgendaDetailActivity extends MaterialActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.agenda_detail_activity;
    }
}
