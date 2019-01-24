package com.joe.base.net;

import io.reactivex.observers.DisposableObserver;

/**
 * @author Joe
 * @time 11/2/2018 11:23 AM
 */
public abstract class MyObserver<T> extends DisposableObserver<T> {
    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, 0));
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);
}

