package com.joe.base.net;

import com.joe.base.bean.GirlsData;
import com.joe.base.bean.NewsData;
import com.joe.base.bean.RequestBean;
import com.joe.common.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Joe
 * @time 7/20/2018 10:59 AM
 */
public class HttpRequestHelper {
    private static IRestfulAPI RESTAPI = HttpHelper.self().getRestfulService();

    public static void getNews(String size, String index, RequestCallBack<NewsData> callBack) {
        requestData(RESTAPI.getNewsData(size, index), callBack);
    }

    public static void getGirls(String size, String index, RequestCallBack<GirlsData> callBack) {
        requestData(RESTAPI.getGirlsData(size, index), callBack);
    }

    /***
     * 用于请求服务端返回非继承于RequestBean数据结构的请求
     * @param service
     * @param callBack
     * @param <T>
     */
    private static <T> void requestData(Observable<T> service, RequestCallBack<T> callBack) {
        service
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(new HttpResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<T>() {
                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.e(ex.code + ":" + ex.message);
                        if (callBack != null)
                            callBack.error(ex.message);
                    }

                    @Override
                    public void onNext(T t) {
                        if (null != t && callBack != null) {
                            callBack.success(t);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /***
     * 用于外部第三方请求
     * @param service
     * @param callBack
     * @param <T>
     */
    private static <T> void requestOther(Observable<T> service, RequestCallBack<T> callBack) {
        service.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {

                    @Override
                    public void onNext(T t) {
                        if (null != t && callBack != null) {
                            callBack.success(t);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (callBack != null) callBack.error(t.getMessage());
                    }
                });
    }

    /***
     * 用于请求服务端返回RequestBean基本数据类型的请求
     * @param service
     * @param callBack
     * @param <T>
     */
    private static <T extends RequestBean> void request(Observable<T> service, RequestCallBack<T> callBack) {
        service
                .subscribeOn(Schedulers.io())
                .map(new ServerResultFunc())
                .onErrorResumeNext(new HttpResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<T>() {
                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.e(ex.code + ":" + ex.message);
                        if (null != callBack) {
                            callBack.error(ex.message);
                        }
                    }

                    @Override
                    public void onNext(T t) {
                        if (null != t && callBack != null) {
                            callBack.success(t);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
