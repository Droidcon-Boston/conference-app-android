package com.mentalmachines.droidcon_boston.views.agenda;

import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;
import com.mentalmachines.droidcon_boston.views.base.BaseContract;
import com.mentalmachines.droidcon_boston.views.base.BasePresenterInterface;

import java.util.List;

/**
 * Created by jinn on 3/29/17.
 */

public interface AgendaContract extends BaseContract {
    interface View {
        void showSchedule(List<DroidconSchedule> schedule);

        void showProgress(boolean progress);

        void showError(Throwable throwable);
    }

    interface Presenter extends BasePresenterInterface {
        void getSchedule();
    }
}
