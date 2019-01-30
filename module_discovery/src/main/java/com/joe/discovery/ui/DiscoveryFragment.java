package com.joe.discovery.ui;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.joe.base.BaseActivity;
import com.joe.base.BaseFragment;
import com.joe.base.bean.NewsData;
import com.joe.base.router.RouterFragmentPath;
import com.joe.discovery.BR;
import com.joe.discovery.R;
import com.joe.discovery.adapter.NewsAdapter;
import com.joe.discovery.databinding.DiscoveryLayoutBinding;
import com.joe.discovery.ui.viewmodel.DiscoveryViewModel;

/**
 * desc: DiscoveryFragment.java
 * author: Joe
 * created at: 2019/1/30 上午10:21
 */
@Route(path = RouterFragmentPath.Discover.PAGER_DISCOVERY)
public class DiscoveryFragment extends BaseFragment<DiscoveryLayoutBinding, DiscoveryViewModel, BaseActivity> {
    private NewsAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.discovery_layout;
    }

    @Override
    protected int getViewModelId() {
        return BR.viewModel;
    }


    @Override
    protected void initView() {
        mAdapter = new NewsAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.recyclerView.setAdapter(mAdapter);
        subscribeToModel(viewModel);
    }

    private void subscribeToModel(final DiscoveryViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveData().observe(this, new Observer<NewsData>() {
            @Override
            public void onChanged(@Nullable NewsData newsData) {
                if (null == newsData) return;
                model.setUiObservableDate(newsData);
                if (binding != null) {
                    mAdapter.setNewsList(newsData.getResults());
                }
            }
        });
    }
}
