package com.mentalmachines.droidcon_boston.views.agenda;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.ScheduleEvent;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase.ScheduleRow;
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper;
import com.mentalmachines.droidcon_boston.utils.StringUtils;
import com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment;

import eu.davidea.flexibleadapter.FlexibleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment for an agenda day
 */
public class AgendaDayFragment extends Fragment {

    private static final String TAG = AgendaDayFragment.class.getName();
    private static final Gson gson = new Gson();

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private Map<String, ScheduleAdapterItemHeader> timeHeaders = new HashMap<>();

    private static final String ARG_DAY = "day";

    private String dayFilter;
    private FirebaseHelper firebaseHelper;

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

        firebaseHelper = new FirebaseHelper();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // onBackPressed();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agenda_day_fragment, container, false);
        ButterKnife.bind(this, view);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        fetchScheduleData(dayFilter);

        return view;
    }

    private void fetchScheduleData(String dayFilter) {
        firebaseHelper.getMainDatabase().child("conferenceData").child("events")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ScheduleRow> rows = new ArrayList<>();
                for (DataSnapshot roomSnapshot: dataSnapshot.getChildren()) {
                    ScheduleEvent data = roomSnapshot.getValue(ScheduleEvent.class);
                    Log.d(TAG, "Event: " + data);
                    if (data != null) {
                        final ScheduleRow scheduleRow = data.toScheduleRow();
                        if (scheduleRow.date.equals(dayFilter)) {
                            rows.add(scheduleRow);
                        }
                    }
                }

                setupHeaderAdapter(rows);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "scheduleQuery:onCancelled", databaseError.toException());
            }
        });

    }

    private void setupHeaderAdapter(List<ScheduleRow> rows) {
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

        Collections.sort(items, (s1, s2) -> {
            int timeComparison = s1.getStartTime().compareTo(s2.getStartTime());
            if (timeComparison != 0) {
                return timeComparison;
            }
            return s1.getRoomSortOrder().compareTo(s2.getRoomSortOrder());
        });

        FlexibleAdapter.enableLogs(true);
        FlexibleAdapter<ScheduleAdapterItem> headerAdapter =
                new FlexibleAdapter<>(items,
                        (FlexibleAdapter.OnItemClickListener) position -> {
                            Object listItem = items.get(position);
                            //noinspection ConstantConditions
                            if (listItem instanceof ScheduleAdapterItem) {
                                ScheduleAdapterItem item = (ScheduleAdapterItem) listItem;
                                final ScheduleRow itemData = item.getItemData();
                                if (StringUtils.isNullorEmpty(itemData.speakerName)) {
                                    String url = itemData.photo;
                                    if (itemData.photo == null) {
                                        return false;
                                    }
                                    // event where info URL is in the photo string
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    PackageManager packageManager = getActivity().getPackageManager();
                                    if (i.resolveActivity(packageManager) != null) {
                                        startActivity(i);
                                    }
                                    return false;
                                }
                                Bundle arguments = new Bundle();
                                arguments.putString(ScheduleDatabase.SCHEDULE_ITEM_ROW, gson.toJson(itemData, ScheduleRow.class));

                                AgendaDetailFragment agendaDetailFragment = new AgendaDetailFragment();
                                agendaDetailFragment.setArguments(arguments);

                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .add(R.id.fragment_container, agendaDetailFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }

                            return true;
                        });
        headerAdapter.expandItemsAtStartUp()
                .setDisplayHeadersAtStartUp(true)
                .setStickyHeaders(true);
        recycler.setAdapter(headerAdapter);
    }
}
