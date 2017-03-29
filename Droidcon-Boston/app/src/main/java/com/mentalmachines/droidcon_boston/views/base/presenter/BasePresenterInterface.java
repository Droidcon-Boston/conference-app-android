package com.mentalmachines.droidcon_boston.views.base.presenter;

import com.mentalmachines.droidcon_boston.views.base.BaseView;

/**
 * Created by jinn on 3/12/17.
 */
public interface BasePresenterInterface<T extends BaseView> {

    void attachView(T mvpView);

    void detachView();
}