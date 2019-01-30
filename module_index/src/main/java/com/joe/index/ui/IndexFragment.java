package com.joe.index.ui;


import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.joe.base.BaseActivity;
import com.joe.base.BaseFragment;
import com.joe.base.bean.GirlsData;
import com.joe.base.router.RouterFragmentPath;
import com.joe.index.BR;
import com.joe.index.R;
import com.joe.index.adapter.IndexAdapter;
import com.joe.index.databinding.IndexFragmentBinding;
import com.joe.index.ui.viewmodel.IndexViewModel;


/**
 * desc: IndexFragment.java
 * author: Joe
 * created at: 2018/12/27 下午4:25
 */
@Route(path = RouterFragmentPath.Home.PAGER_INDEX)
public class IndexFragment extends BaseFragment<IndexFragmentBinding, IndexViewModel, BaseActivity> {
    private IndexAdapter mAdapter;

    private void subscribeToModel(final IndexViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveData().observe(this, new Observer<GirlsData>() {
            @Override
            public void onChanged(@Nullable GirlsData newsData) {
                if (null == newsData) return;
                model.setUiObservableDate(newsData);
                if (binding != null) {
                    mAdapter.setGirlsList(newsData.getResults());
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment;
    }

    @Override
    protected int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        mAdapter = new IndexAdapter();
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        binding.recyclerView.setAdapter(mAdapter);
        subscribeToModel(viewModel);
    }
}
