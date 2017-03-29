package com.mentalmachines.droidcon_boston.views.agenda;

import com.mentalmachines.droidcon_boston.data.DataManager;
import com.mentalmachines.droidcon_boston.views.base.presenter.BasePresenter;
import com.mentalmachines.droidcon_boston.views.base.BaseView;

import timber.log.Timber;


/**
 * Created by CaptofOuterSpace on 8/22/2016.
 */
public class AgendaPresenter extends BasePresenter<AgendaView> {
    AgendaFragment mView;
    DataManager dataManager;

    public AgendaPresenter(AgendaFragment agendaFragment, DataManager dataManager) {
        mView = agendaFragment;
        dataManager = dataManager;
    }

    @Override
    public void detachView() {

    }

    public void getSchedule() {
        dataManager.getSchedule()
                .subscribe(schedule -> {
                }, e -> {
                    Timber.e(e.toString());
                });
    }


}
