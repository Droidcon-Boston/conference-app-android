package com.mentalmachines.droidcon_boston.services;

import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jinn on 3/15/17.
 */

public interface DroidconAgendaApi {
    final String DroidconWordpressApiUrl = "http://www.droidcon-boston.com/wp-json/wp/v2";
    @GET("pages/461")
    Single<List<DroidconSchedule>> getSchedule();

}
