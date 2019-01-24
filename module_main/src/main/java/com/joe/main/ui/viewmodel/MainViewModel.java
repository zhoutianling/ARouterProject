package com.joe.main.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

/**
 * desc: MainViewModel.java
 * author: Joe
 * created at: 2019/1/23 下午3:55
 */
public class MainViewModel extends AndroidViewModel {
    private LiveData observableData;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
}
