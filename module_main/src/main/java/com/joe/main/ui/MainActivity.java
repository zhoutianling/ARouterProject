package com.joe.main.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.joe.base.router.RouterActivityPath;
import com.joe.base.router.RouterFragmentPath;
import com.joe.main.BR;
import com.joe.main.R;
import com.joe.main.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> {
    private List<Fragment> mFragments;

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        Fragment indexFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_INDEX).navigation();
        Fragment discoverFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Discover.PAGER_DISCOVERY).navigation();
        Fragment aboutFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.About.PAGER_ABOUT).navigation();
        mFragments.add(indexFragment);
        mFragments.add(discoverFragment);
        mFragments.add(aboutFragment);
        if (indexFragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, indexFragment).commit();
        }
        NavigationController navigationController = binding.pagerBottomTab.material()
                .addItem(R.mipmap.ic_index, "首页")
                .addItem(R.mipmap.ic_discover, "发现")
                .addItem(R.mipmap.ic_about, "关于")
                .setDefaultColor(ContextCompat.getColor(this, R.color.defaultTextColor))
                .build();
        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Fragment currentFragment = mFragments.get(index);
                if (currentFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, currentFragment);
                    transaction.commitAllowingStateLoss();
                }
            }

            @Override
            public void onRepeat(int index) {
            }
        });
    }

    @Override
    public int initContentView(Bundle bundle) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
