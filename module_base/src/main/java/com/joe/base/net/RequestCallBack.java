package com.joe.base.net;

/**
 * @author Joe
 * @time 9/13/2018 10:38 AM
 */
public interface RequestCallBack<T> {
    void success(T t);

    void error(String s);
}
