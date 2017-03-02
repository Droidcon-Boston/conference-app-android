package com.mentalmachines.droidcon_boston;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

/**
 * Created by jkim11 on 2/24/17.
 */

public class DroidconBostonApplication extends Application{

    public static DroidconBostonApplication get(Context context) {
        return (DroidconBostonApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }
    }
}
