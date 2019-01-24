package com.joe.base.net;

import com.joe.base.bean.RequestBean;

import io.reactivex.functions.Function;

/**
 * @author Joe
 * @des 只针对服务端返回RequestBean 基本类型的数据结构做拦截处理
 * @time 11/2/2018 10:58 AM
 */
public class ServerResultFunc<T extends RequestBean> implements Function<T, T> {
    @Override
    public T apply(T t) {
        if (t.getStatus() == 200) {
            return t;
        } else {
            throw new ServerException(t.getStatus(), t.getMsg());
        }
    }
}
