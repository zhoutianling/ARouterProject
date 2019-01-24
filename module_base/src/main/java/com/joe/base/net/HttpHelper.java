package com.joe.base.net;

import android.content.Context;
import android.support.annotation.NonNull;

import com.joe.base.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Joe on 2018/7/5.
 */
public class HttpHelper {
    private Context mContext;
    private OkHttpClient okHttpClient;
    private volatile IRestfulAPI restfulAPI;

    private static class HttpHelperHolder {
        private static final HttpHelper INSTANCE = new HttpHelper();
    }

    public static HttpHelper self() {
        return HttpHelperHolder.INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public HttpHelper() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);
            builder.addInterceptor(getHeaderInterceptor());
            builder.addInterceptor(new LoggingInterceptor());
            okHttpClient = builder.build();
        }
    }


    @NonNull
    private Interceptor getHeaderInterceptor() {
        return chain -> {
            Request newRequest = chain.request().newBuilder().addHeader("device", "androidId").build();
            return chain.proceed(newRequest);
        };
    }


    public IRestfulAPI getRestfulService() {
        if (restfulAPI == null) {
            synchronized (this) {
                if (restfulAPI == null) {
                    restfulAPI = new Retrofit.Builder()
                            .baseUrl(AppConstants.baseURl)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build().create(IRestfulAPI.class);
                }
            }
        }
        return restfulAPI;
    }

}

