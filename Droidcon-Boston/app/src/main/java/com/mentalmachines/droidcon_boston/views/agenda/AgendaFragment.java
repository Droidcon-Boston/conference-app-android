package com.mentalmachines.droidcon_boston.views.agenda;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.DataManager;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;
import com.mentalmachines.droidcon_boston.views.base.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;

import static com.mentalmachines.droidcon_boston.services.MvpServiceFactory.makeMvpStarterService;

/**
 * Created by jinn on 3/11/17.
 */

public class AgendaFragment extends BaseFragment implements AgendaContract.View {

    @BindView(R.id.tablayout)
    android.support.design.widget.TabLayout tabLayout;

    @BindView(R.id.viewpager)
    android.support.v4.view.ViewPager viewPager;

    ScheduleAdapter adapter;
    AgendaPresenter presenter;
    DataManager dataManager;

    private Map<String, ScheduleAdapterItemHeader> timeHeaders = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public int getLayout() {
        return R.layout.agenda_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataManager = new DataManager(makeMvpStarterService());
        presenter = new AgendaPresenter(dataManager);

        presenter.getSchedule();

        setupDayPager(view);
    }

    public void showSchedule(List<DroidconSchedule> schedule) {
        adapter.setSchedule(schedule);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress(boolean progress) {

    }

    @Override
    public void showError(Throwable throwable) {

    }


    private void setupDayPager(View parent) {
        ViewPager viewPager = (ViewPager) parent.findViewById(R.id.viewpager);
        viewPager.setAdapter(new AgendaDayPagerAdapter(getChildFragmentManager()));

        TabLayout tabLayout = (TabLayout) parent.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        // set current day to second if today matches
        Calendar today = Calendar.getInstance();
        Calendar dayTwo = Calendar.getInstance();
        dayTwo.set(2017, 04, 10);
        if (today.equals(dayTwo)) {
            viewPager.setCurrentItem(1);
        }
    }
}
