package com.joe.main.ui;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.joe.base.bean.BaseViewModel;
import com.joe.base.binding.command.BindingAction;
import com.joe.base.binding.command.BindingCommand;

/**
 * desc: LoadingModel.java
 * author: Joe
 * created at: 2019/1/6 下午8:31
 */
public class LoadingModel extends BaseViewModel {
    public LoadingModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand startMainCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Log.i("zzz","call");
            startActivity(MainActivity.class);
            finish();
        }
    });
    public View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("zzz","call");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
