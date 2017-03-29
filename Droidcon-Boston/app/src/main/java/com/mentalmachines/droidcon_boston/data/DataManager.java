package com.mentalmachines.droidcon_boston.data;

import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;
import com.mentalmachines.droidcon_boston.services.DroidconAgendaApi;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jinn on 3/28/17.
 */

public class DataManager {
    private final DroidconAgendaApi droidconAgendaApi;

    public DataManager(DroidconAgendaApi droidconAgendaApi) {
        this.droidconAgendaApi = droidconAgendaApi;
    }

    public Single<List<DroidconSchedule>> getSchedule() {
        return droidconAgendaApi.getSchedule();
    }

}
