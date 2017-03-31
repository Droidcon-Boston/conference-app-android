package com.mentalmachines.droidcon_boston.views.agenda;

import com.mentalmachines.droidcon_boston.data.DataManager;
import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;
import com.mentalmachines.droidcon_boston.views.base.BasePresenter;

import java.util.List;

import io.reactivex.Observable;
import timber.log.Timber;


/**
 * Created by CaptofOuterSpace on 8/22/2016.
 */
public class AgendaPresenter extends BasePresenter<AgendaContract.View> implements AgendaContract.Presenter {
    DataManager dataManager;

    public AgendaPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
    }


    public void getSchedule() {
        dataManager.getSchedule()
                .subscribe(schedule -> {
                    getView().showProgress(false);
                    getView().showSchedule(schedule);
                }, e -> {
                    getView().showProgress(false);
                    getView().showError(e);
                    Timber.e(e.toString());
                });
    }

    @Override
    public Observable<List<DroidconSchedule>> observeSchedule() {
        return null;
    }
}
