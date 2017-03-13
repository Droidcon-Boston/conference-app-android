package com.mentalmachines.droidcon_boston.presenter;

import android.location.Location;

import com.mentalmachines.droidcon_boston.views.AgendaFragment;
import com.mentalmachines.droidcon_boston.views.base.BaseView;


/**
 * Created by CaptofOuterSpace on 8/22/2016.
 */
public class AgendaPresenter implements AgendaPresenterInterface {
    AgendaFragment mView;
    // OpenWeatherMapService mOpenWeatherMapService;

    public AgendaPresenter(AgendaFragment agendaFragment) {
        mView = agendaFragment;
        // mOpenWeatherMapService = new OpenWeatherMapService();
    }

    @Override
    public void attachView(BaseView mvpView) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getAgenda() {

    }
}
