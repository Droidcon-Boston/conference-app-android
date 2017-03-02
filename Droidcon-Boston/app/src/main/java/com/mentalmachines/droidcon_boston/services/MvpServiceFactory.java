package com.mentalmachines.droidcon_boston.services;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mentalmachines.mvptemplate.BuildConfig;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.MoshiConverterFactory;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by jkim11 on 2/13/17.
 */

public class MvpServiceFactory {
    public static MvpServiceFactory makeMvpStarterService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API_URL)
                .client(makeOkHttpClient())
                .addConverterFactory(MoshiConverterFactory.create(makeMoshi()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(OpenWeatherMapApi.class);
    }

    private static OkHttpClient makeOkHttpClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message
                    -> Timber.d(message));
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
            httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        }

        return httpClientBuilder.build();
    }

    private static Moshi makeMoshi() {
        return new Moshi.Builder().create();
    }

}
