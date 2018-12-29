package com.joe.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * desc: BaseActivity.java
 * author: Joe
 * created at: 2018/12/29 下午2:26
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected RxPermissions rxPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        rxPermissions = new RxPermissions(this);
        checkPermission();
        initView();
        requestData();
    }

    @SuppressLint("CheckResult")
    private void checkPermission() {
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();


    protected abstract void requestData();
}
