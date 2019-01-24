package com.joe.discovery.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.joe.base.BaseFragment;
import com.joe.base.bean.NewsData;
import com.joe.base.router.RouterFragmentPath;
import com.joe.discovery.R;
import com.joe.discovery.adapter.NewsAdapter;
import com.joe.discovery.databinding.DiscoveryLayoutBinding;
import com.joe.discovery.databinding.DiscoveryNewsItemBinding;
import com.joe.discovery.ui.viewmodel.DiscoveryViewModel;

/**
 * Created by ATiana on 2018/12/28.
 */
@Route(path = RouterFragmentPath.Discover.PAGER_DISCOVERY)
public class DiscoveryFragment extends BaseFragment {
    private NewsAdapter mAdatper;
    private DiscoveryLayoutBinding binding;

    @Override
    protected int getLayoutId() {
        return R.layout.discovery_layout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.discovery_layout, container, false);
        mAdatper = new NewsAdapter();
        DiscoveryViewModel model = ViewModelProviders.of(this).get(DiscoveryViewModel.class);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.recyclerView.setAdapter(mAdatper);
        subscribeToModel(model);
        return binding.getRoot();
    }

    @Override
    protected void initView() {
    }

    private void subscribeToModel(final DiscoveryViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveData().observe(this, new Observer<NewsData>() {
            @Override
            public void onChanged(@Nullable NewsData newsData) {
                if (null == newsData) return;
                model.setUiObservableDate(newsData);
                if (binding != null) {
                    mAdatper.setGirlsList(newsData.getResults());
                }
            }
        });
    }

    @Override
    protected void requestData() {

    }
}
