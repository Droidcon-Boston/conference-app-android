package com.mentalmachines.droidcon_boston.views.agenda;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Fragment for an agenda day
 */
public class AgendaDayFragment extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private Map<String, ScheduleAdapterItemHeader> timeHeaders = new HashMap<>();

    private static final String ARG_DAY = "day";

    private String dayFilter;

    public AgendaDayFragment() {
        // Required empty public constructor
    }

    public static AgendaDayFragment newInstance(String day) {
        AgendaDayFragment fragment = new AgendaDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayFilter = getArguments().getString(ARG_DAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agenda_day_fragment, container, false);
        ButterKnife.bind(this, view);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        setupHeaderAdapter();

        return view;
    }


    private void setupHeaderAdapter() {
        List<ScheduleDatabase.ScheduleRow> rows = ScheduleDatabase.fetchScheduleListByDay(getContext(), dayFilter);
        List<ScheduleAdapterItem> items = new ArrayList<>(rows.size());
        for (ScheduleDatabase.ScheduleRow row : rows) {
            String timeDisplay = ((row.time == null) || (row.time.length() == 0)) ? "Unscheduled" : row.time;
            ScheduleAdapterItemHeader header = timeHeaders.get(timeDisplay);
            if (header == null) {
                header = new ScheduleAdapterItemHeader(timeDisplay);
                timeHeaders.put(timeDisplay, header);
            }

            ScheduleAdapterItem item = new ScheduleAdapterItem(row, header);
            items.add(item);
        }

        FlexibleAdapter.enableLogs(true);
        FlexibleAdapter<ScheduleAdapterItem> headerAdapter =
                new FlexibleAdapter<>(items,
                        (FlexibleAdapter.OnItemClickListener) position -> {
                            ScheduleAdapterItem item = items.get(position);

                            AgendaDetailFragment agendaDetailFragment = new AgendaDetailFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle arguments = new Bundle();
                            arguments.putString("speaker_name", item.getItemData().speakerName);
                            agendaDetailFragment.setArguments(arguments);
                            fragmentTransaction.add(R.id.fragment_container, agendaDetailFragment);
                            fragmentTransaction.commit();
                            return true;
                        });
        headerAdapter
                .expandItemsAtStartUp()
                .setDisplayHeadersAtStartUp(true)
                .setStickyHeaders(true);
        recycler.setAdapter(headerAdapter);
    }
}
