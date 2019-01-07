package com.joe.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.joe.base.BaseActivity;
import com.joe.main.BR;
import com.joe.main.R;
import com.joe.main.databinding.ActivityLoadingBinding;

public class LoadingActivity extends BaseActivity<ActivityLoadingBinding, LoadingModel> {


    @Override
    protected int getToolbarId() {
        return 0;
    }

    @Override
    protected int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_loading;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                finish();
            }
        }, 200);
    }


}
