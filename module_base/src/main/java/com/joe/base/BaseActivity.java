package com.joe.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.joe.base.bean.BaseViewModel;
import com.joe.base.bus.Messenger;
import com.joe.commom_library.base.BaseDialog;
import com.joe.commom_library.utils.BlurBitmapUtils;
import com.joe.commom_library.utils.Utils;
import com.joe.commom_library.widget.dialog.WaitDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * desc: BaseActivity.java
 * author: Joe
 * created at: 2018/12/29 下午2:26
 */

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity {
    protected V binding;
    protected VM viewModel;
    protected int viewModelId;
    protected RxPermissions rxPermissions;
    protected Toolbar toolbar;
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
        rxPermissions = new RxPermissions(this);
        checkPermission();
        initViewDataBinding(savedInstanceState);
        registorUIChangeLiveDataCallBack();
        initView();
        viewModel.registerRxBus();
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
     * @param hideTitle 是否隐藏Title
     */
    protected void setupToolBar(boolean hideTitle) {
        toolbar = findViewById(getToolbarId());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (getToolbarId() > 0 && actionBar != null) {
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

    private void registorUIChangeLiveDataCallBack() {
        //加载对话框消失
        viewModel.getUC().getDismissDialogEvent().observe(this, v -> dismissDialog());
        //跳入新页面
        viewModel.getUC().getStartActivityEvent().observe(this, params -> {
            Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
            Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
            startActivity(clz, bundle);
        });
        //关闭界面
        viewModel.getUC().getFinishEvent().observe(this, v -> finish());
        //关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, v -> onBackPressed());
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除Messenger注册
        Messenger.getDefault().unregister(viewModel);
        //解除ViewModel生命周期感应
        getLifecycle().removeObserver(viewModel);
        viewModel.removeRxBus();
        viewModel = null;
        binding.unbind();
    }


    protected abstract int getToolbarId();

    protected abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    protected abstract void initView();

    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected BaseDialog dialog;

    public void showLoadingDialog(String content) {
        dialog = new WaitDialog.Builder(this)
                .setMessage(content)
                .show();
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }

    public void dismissDialog() {
        dialog.dismiss();
    }


}
