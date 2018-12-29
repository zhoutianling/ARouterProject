package com.joe.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * desc: BaseFragment.java
 * author: Joe
 * created at: 2018/12/29 下午4:00
 */
abstract class BaseFragment<T extends BaseActivity> extends Fragment {
    protected View rootView;
    protected Activity mActivty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            initView();
            requestData();
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivty = getActivity();
    }

    protected T getPatentActivity() {
        return (T) getActivity();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();


    protected abstract void requestData();
}
