package com.joe.discovery.ui.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.joe.base.bean.BaseViewModel;
import com.joe.base.bean.NewsData;
import com.joe.base.net.HttpRequestHelper;
import com.joe.base.net.RequestCallBack;
import com.joe.common.utils.NetUtils;

import io.reactivex.disposables.CompositeDisposable;

/**
 * desc: DiscoveryViewModel.java
 * author: Joe
 * created at: 2019/1/23 下午4:04
 */
public class DiscoveryViewModel extends BaseViewModel {
    private static final MutableLiveData data = new MutableLiveData();

    {
        data.setValue(null);
    }

    private LiveData<NewsData> liveData;
    public ObservableField<NewsData> uiObservableDate = new ObservableField<>();

    private final CompositeDisposable disposable = new CompositeDisposable();


    public DiscoveryViewModel(@NonNull Application application) {
        super(application);
        liveData = Transformations.switchMap(NetUtils.netConnected(application), new Function<Boolean, LiveData<NewsData>>() {
            @Override
            public LiveData<NewsData> apply(Boolean input) {
                if (!input) {
                    return data;
                } else {
                    final MutableLiveData<NewsData> applyData = new MutableLiveData<>();
                    HttpRequestHelper.getNews("50", "1", new RequestCallBack<NewsData>() {
                        @Override
                        public void success(NewsData newsData) {
                            if (newsData != null) {
                                Log.i("zzz", "news data:" + newsData.getResults().size());
                                applyData.setValue(newsData);
                            } else {
                                Log.i("zzz", "news data null");
                            }
                        }

                        @Override
                        public void error(String s) {
                            Log.i("zzz", "news data error:" + s);
                        }
                    });
                    return applyData;
                }
            }
        });

    }

    public LiveData<NewsData> getLiveData() {
        return liveData;
    }

    public void setUiObservableDate(NewsData uiObservableDate) {
        this.uiObservableDate.set(uiObservableDate);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
