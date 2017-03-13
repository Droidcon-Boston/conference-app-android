package com.mentalmachines.droidcon_boston.presenter;

import com.mentalmachines.droidcon_boston.views.base.BaseView;

/**
 * Created by jinn on 3/12/17.
 */
public interface BasePresenterInterface<V extends BaseView> {

    void attachView(V mvpView);

    void detachView();
}