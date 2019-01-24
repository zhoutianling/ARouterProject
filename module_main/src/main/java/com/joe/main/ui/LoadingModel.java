package com.joe.main.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

/**
 * desc: LoadingModel.java
 * author: Joe
 * created at: 2019/1/6 下午8:31
 */
public class LoadingModel extends AndroidViewModel {
    public LoadingModel(@NonNull Application application) {
        super(application);
    }

    public View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("zzz", "call");
        }
    };

}
