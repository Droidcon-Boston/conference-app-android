package com.mentalmachines.droidcon_boston.views.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by jinn on 2/1/18.
 */

public abstract class MaterialFragment<P extends BaseContract.Presenter, V extends BaseContract.View> extends
    Fragment {

    P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public getLayout() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void attachPresenter(Object o, P presenter) {
        if (presenter != null) {
            // presenter.attachLifecycle(lifecycle());

            /*lifecycleLoggingSubsbcription = lifeCycleSubject.subscribe(() -> {
                Timber.d("Lifecycle :: " + o.getClass().getSimpleName());
            });*/

            // lifeCycleSubject.onNext(@LifecycleEvent String event);
        }
    }

    @LayoutRes
    public abstract int getLayout();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
