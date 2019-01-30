package com.joe.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.base.bean.BaseViewModel;
import com.joe.common.utils.Utils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * desc: BaseFragment.java
 * author: Joe
 * created at: 2018/12/29 下午4:00
 */
public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel, T extends BaseActivity> extends RxFragment {

    protected B binding;
    protected VM viewModel;
    protected View rootView;
    protected BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null && getLayoutId() > 0) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            initViewModel();
            rootView = binding.getRoot();
            initView();
        }
        return rootView;
    }

    private void initViewModel() {
        if (this.viewModel == null) {
            Type type = this.getClass().getGenericSuperclass();
            Class modelClass;
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                modelClass = BaseViewModel.class;
            }

            viewModel = (VM) createViewModel(this, modelClass);
        }
        binding.setVariable(getViewModelId(), viewModel);
        getLifecycle().addObserver(this.viewModel);
        viewModel.injectLifecycleProvider(this);
    }

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) rootView.findViewById(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    protected T getPatentActivity() {
        return (T) mActivity;
    }


    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }

    protected abstract int getLayoutId();

    protected abstract int getViewModelId();


    protected abstract void initView();


}
