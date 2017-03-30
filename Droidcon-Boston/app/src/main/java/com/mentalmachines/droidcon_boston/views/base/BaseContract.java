package com.mentalmachines.droidcon_boston.views.base;

/**
 * Created by jinn on 3/29/17.
 */

public interface BaseContract {
    interface Presenter<V extends BaseView> {

        void attachView(V view);

        void detachView();
    }

    interface BaseView {

    }
}
