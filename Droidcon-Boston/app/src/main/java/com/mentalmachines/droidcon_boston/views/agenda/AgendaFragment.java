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

import com.mentalmachines.droidcon_boston.R;

import com.mentalmachines.droidcon_boston.views.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jinn on 3/11/17.
 */

public class AgendaFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    ScheduleAdapter adapter;
    AgendaPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(getLayout(), container,
                false);

        ButterKnife.bind(getActivity());

        return rootView;
    }

    @Override
    public int getLayout() {
        return R.layout.agenda_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.getSchedule();
        });

        presenter.getSchedule();


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showSchedule(){
        // adapter.setSchedule(schedule);
        adapter.notifyDataSetChanged();

        recycler.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }
}
