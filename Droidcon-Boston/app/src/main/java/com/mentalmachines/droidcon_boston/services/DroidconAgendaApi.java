package com.mentalmachines.droidcon_boston.services;

import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by jinn on 3/15/17.
 */

public interface DroidconAgendaApi {
    @GET("pages/461")
    Single<List<DroidconSchedule>> getSchedule();
}
