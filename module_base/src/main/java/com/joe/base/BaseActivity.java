package com.joe.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.joe.commom_library.base.BaseDialog;
import com.joe.commom_library.utils.BlurBitmapUtils;
import com.joe.commom_library.utils.Utils;
import com.joe.commom_library.widget.dialog.MessageDialog;
import com.joe.commom_library.widget.dialog.WaitDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * desc: BaseActivity.java
 * author: Joe
 * created at: 2018/12/29 下午2:26
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected RxPermissions rxPermissions;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 获取一个 Handler 对象
     */
    public static Handler getHandler() {
        return HANDLER;
    }

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

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    /***
     * 设置高斯模糊背景
     * @param rootView
     */
    public void setBlueBg(View rootView) {
        Bitmap bitmap = BlurBitmapUtils.getBlurBitmap(this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_user));
        BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootView.setBackground(drawable);
        } else {
            rootView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected void setupToolBar(Toolbar toolbar, boolean hideTitle) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public void finish() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        super.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();


    protected abstract void requestData();

    protected BaseDialog dialog;

    public void showLoadingDialog(String content) {
        dialog = new WaitDialog.Builder(this)
                .setMessage(content)
                .show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
