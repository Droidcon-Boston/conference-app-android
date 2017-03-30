package com.mentalmachines.droidcon_boston.views.base;

/**
 * Created by jinn on 3/12/17.
 */


import rx.Observable;
import rx.Single;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */

public class BasePresenter<T> implements BasePresenterInterface<T> {

    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    private static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

    /**
     * Encapsulate the result of an rx Observable.
     * This model is meant to be used by the children presenters to easily keep a reference
     * to the latest loaded result so that it can be easily emitted again when on configuration
     * changes.
     */
    protected static class DataResult<T> {

        private T mData;
        private Throwable mError;

        public DataResult(T data) {
            mData = data;
        }

        public DataResult(Throwable error) {
            mError = error;
        }

        public Single<T> toSingle() {
            if (mError != null) {
                return Single.error(mError);
            }
            return Single.just(mData);
        }

        public Observable<T> toObservable() {
            if (mError != null) {
                return Observable.error(mError);
            }
            return Observable.just(mData);
        }
    }


}
