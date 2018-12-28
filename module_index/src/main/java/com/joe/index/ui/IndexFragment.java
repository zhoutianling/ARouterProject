package com.joe.index.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.joe.index.R;
import com.joe.base.router.RouterFragmentPath;
/**
 * desc: IndexFragment.java
 * author: Joe
 * created at: 2018/12/27 下午4:25
 */
@Route(path = RouterFragmentPath.Home.PAGER_INDEX)
public class IndexFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_fragment, container, false);
        return view;
    }
}
