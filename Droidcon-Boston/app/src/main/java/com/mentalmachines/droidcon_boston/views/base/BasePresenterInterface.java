package com.mentalmachines.droidcon_boston.views.base;

public interface BasePresenterInterface<V> {
    void attachView(V view);

    void detachView();
}