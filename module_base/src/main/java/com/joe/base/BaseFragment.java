package com.joe.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.commom_library.utils.Utils;

/**
 * desc: BaseFragment.java
 * author: Joe
 * created at: 2018/12/29 下午4:00
 */
public abstract class BaseFragment<T extends BaseActivity> extends Fragment {
    protected View rootView;
    protected BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null && getLayoutId() > 0) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            initView();
            requestData();
        }
        return rootView;
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

    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getPatentActivity().addFragment(fragment, frameId);

    }

    protected abstract int getLayoutId();

    protected abstract void initView();


    protected abstract void requestData();
}
