package com.mentalmachines.droidcon_boston.views.base;


public interface BaseContract {
    interface Presenter<V extends BaseView> {

        void attachView(V view);

        void detachView();
    }

    interface BaseView {

    }
}
