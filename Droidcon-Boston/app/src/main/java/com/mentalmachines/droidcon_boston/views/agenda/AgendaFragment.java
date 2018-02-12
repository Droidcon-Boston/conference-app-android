package com.mentalmachines.droidcon_boston.views.agenda;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mentalmachines.droidcon_boston.R;
import java.util.Calendar;

public class AgendaFragment extends Fragment {

  public static final String TAB_POSITION = "POSITION";

  @BindView(R.id.tablayout)
  android.support.design.widget.TabLayout tabLayout;

  @BindView(R.id.viewpager)
  android.support.v4.view.ViewPager viewPager;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.agenda_fragment, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }


  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupDayPager(view, savedInstanceState);
  }

  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(TAB_POSITION, tabLayout.getSelectedTabPosition());
  }

  private void setupDayPager(View parent, Bundle savedInstanceState) {
    ViewPager viewPager = parent.findViewById(R.id.viewpager);
    viewPager.setAdapter(new AgendaDayPagerAdapter(getChildFragmentManager()));

    TabLayout tabLayout = parent.findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);

    if (savedInstanceState != null) {
      viewPager.setCurrentItem(savedInstanceState.getInt(TAB_POSITION));
    } else {
      // set current day to second if today matches
      Calendar today = Calendar.getInstance();
      Calendar dayTwo = Calendar.getInstance();
      dayTwo.set(2017, Calendar.APRIL, 11);
      if (today.equals(dayTwo)) {
        viewPager.setCurrentItem(1);
      }
    }
  }
}
