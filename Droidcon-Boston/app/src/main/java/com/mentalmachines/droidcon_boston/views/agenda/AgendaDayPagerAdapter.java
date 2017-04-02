package com.mentalmachines.droidcon_boston.views.agenda;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;

public class AgendaDayPagerAdapter extends FragmentPagerAdapter {
    @SuppressWarnings("FieldCanBeLocal")
    private final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Day 1", "Day 2" };

    public AgendaDayPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return AgendaDayFragment.newInstance(
                (position == 0) ? ScheduleDatabase.MONDAY : ScheduleDatabase.TUESDAY
        );
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}