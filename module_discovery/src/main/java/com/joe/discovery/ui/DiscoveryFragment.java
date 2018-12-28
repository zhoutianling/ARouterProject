package com.joe.discovery.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.joe.base.router.RouterFragmentPath;
import com.joe.discovery.R;

/**
 * Created by ATiana on 2018/12/28.
 */
@Route(path = RouterFragmentPath.Discover.PAGER_DISCOVERY)
public class DiscoveryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discovery_layout, container, false);
    }
}
