package com.mentalmachines.droidcon_boston.views.base;

/**
 * Created by jinn on 3/12/17.
 */
public interface BasePresenterInterface<V> {
    void attachView(V view);

    void detachView();
}