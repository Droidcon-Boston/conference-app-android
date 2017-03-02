package com.mentalmachines.droidcon_boston.presenter;

import android.location.Location;

import com.mentalmachines.droidcon_boston.views.WeatherFragment;


/**
 * Created by CaptofOuterSpace on 8/22/2016.
 */
public class Presenter implements PresenterInterface {
    WeatherFragment mView;
    // OpenWeatherMapService mOpenWeatherMapService;

    public Presenter(WeatherFragment weatherFragment) {
        mView = weatherFragment;
        // mOpenWeatherMapService = new OpenWeatherMapService();
    }

    public void getCurrentWeather(Location location) {
        /*mOpenWeatherMapService.getOpenWeatherMapApi()
                .getCurrentWeather("Boston,US", "imperial")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentWeather -> mView.updateCurrentWeatherViews(currentWeather)});*/
    }
}
