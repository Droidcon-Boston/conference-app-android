package com.mentalmachines.droidcon_boston.views.agenda;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

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

        /*
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case ScheduleAdapter.TYPE_GENERAL:
                        return 1;
                    case ScheduleAdapter.TYPE_SINGLE_ITEM:
                        return 1;
                    case ScheduleAdapter.TYPE_DOUBLE_ITEM:
                        return 2;
                    case ScheduleAdapter.TYPE_TRIPLE_ITEM:
                        return 3;
                    default:
                        return -1;
                }
            }
        });
        recycler.setLayoutManager(gridLayoutManager);
        */

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycler.setAdapter(new ScheduleDatabase.ScheduleAdapter(getContext()));
        setupHeaderAdapter();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.getSchedule();
        });

        presenter.getSchedule();
    }

    private void setupHeaderAdapter() {
        List<ScheduleDatabase.ScheduleRow> rows = ScheduleDatabase.fetchScheduleListByDay(getContext(), null);
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
                        new FlexibleAdapter.OnItemClickListener() {
                            @Override
                            public boolean onItemClick(int position) {
                                ScheduleAdapterItem item = items.get(position);

                                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
        headerAdapter
                        .expandItemsAtStartUp()
                        .setDisplayHeadersAtStartUp(true)
                        .setStickyHeaders(true);
        recycler.setAdapter(headerAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showSchedule(List<DroidconSchedule> schedule) {
        adapter.setSchedule(schedule);
        adapter.notifyDataSetChanged();

        recycler.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(boolean progress) {

    }

    @Override
    public void showError(Throwable throwable) {

    }
}
