package com.joe.base.net;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Joe
 * @time 11/2/2018 10:18 AM
 */
public class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
