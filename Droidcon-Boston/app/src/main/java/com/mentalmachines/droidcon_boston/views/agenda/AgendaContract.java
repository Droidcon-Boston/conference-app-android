package com.mentalmachines.droidcon_boston.views.agenda;

import com.mentalmachines.droidcon_boston.views.base.BaseContract;
import com.mentalmachines.droidcon_boston.views.base.BasePresenterInterface;

public interface AgendaContract extends BaseContract {
    interface View {

        void showProgress(boolean progress);

        void showError(Throwable throwable);
    }

    interface Presenter extends BasePresenterInterface {
        void getSchedule();
    }
}
