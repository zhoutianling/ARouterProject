package com.joe.index.ui.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.joe.base.GlideInit;
import com.joe.base.bean.BaseViewModel;
import com.joe.base.bean.GirlsData;
import com.joe.base.bean.NewsData;
import com.joe.base.net.HttpRequestHelper;
import com.joe.base.net.RequestCallBack;
import com.joe.common.utils.NetUtils;
import com.joe.index.R;

import io.reactivex.disposables.CompositeDisposable;

/**
 * desc: IndexViewModel.java
 * author: Joe
 * created at: 2019/1/23 下午4:04
 */
public class IndexViewModel extends BaseViewModel {
    private static final MutableLiveData data = new MutableLiveData();

    {
        data.setValue(null);
    }

    private LiveData<GirlsData> liveData;
    public ObservableField<GirlsData> uiObservableDate = new ObservableField<>();

    private final CompositeDisposable disposable = new CompositeDisposable();


    public IndexViewModel(@NonNull Application application) {
        super(application);
        liveData = Transformations.switchMap(NetUtils.netConnected(application), new Function<Boolean, LiveData<GirlsData>>() {
            @Override
            public LiveData<GirlsData> apply(Boolean input) {
                if (!input) {
                    return data;
                } else {
                    final MutableLiveData<GirlsData> applyData = new MutableLiveData<>();
                    HttpRequestHelper.getGirls("50", "1", new RequestCallBack<GirlsData>() {
                        @Override
                        public void success(GirlsData newsData) {
                            if (newsData != null) {
                                Log.i("zzz", " girls data:" + newsData.getResults().size());
                                applyData.setValue(newsData);
                            } else {
                                Log.i("zzz", "data null");
                            }
                        }

                        @Override
                        public void error(String s) {

                        }
                    });
                    return applyData;
                }
            }
        });

    }

    public LiveData<GirlsData> getLiveData() {
        return liveData;
    }

    public void setUiObservableDate(GirlsData uiObservableDate) {
        this.uiObservableDate.set(uiObservableDate);
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).into(view);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
