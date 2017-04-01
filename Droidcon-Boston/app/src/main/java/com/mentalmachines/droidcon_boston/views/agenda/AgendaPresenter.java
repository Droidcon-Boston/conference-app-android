package com.mentalmachines.droidcon_boston.views.agenda;

import com.mentalmachines.droidcon_boston.data.DataManager;
import com.mentalmachines.droidcon_boston.views.base.BasePresenter;


/**
 * Created by CaptofOuterSpace on 8/22/2016.
 */
public class AgendaPresenter extends BasePresenter<AgendaContract.View> {
    DataManager dataManager;

    public AgendaPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
    }


    public void getSchedule() {
    }
}
