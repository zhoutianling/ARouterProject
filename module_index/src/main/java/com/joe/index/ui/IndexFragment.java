package com.joe.index.ui;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.joe.base.BaseFragment;
import com.joe.base.router.RouterFragmentPath;
import com.joe.commom_library.zxing.activity.CaptureActivity;
import com.joe.index.R;


/**
 * desc: IndexFragment.java
 * author: Joe
 * created at: 2018/12/27 下午4:25
 */
@Route(path = RouterFragmentPath.Home.PAGER_INDEX)
public class IndexFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void requestData() {
    }
}
