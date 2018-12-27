package com.atian.index.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.atian.index.R;
import com.atian.module_base.router.RouterFragmentPath;

/**
 * Created by ATiana on 2018/12/26.
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