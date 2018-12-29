package com.joe.main.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.joe.base.BaseActivity;
import com.joe.base.router.RouterActivityPath;
import com.joe.base.router.RouterFragmentPath;
import com.joe.main.R;

import java.util.Arrays;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends BaseActivity {
    PageNavigationView pageNavigationView;
    private List<Fragment> mFragments;


    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView() {
        pageNavigationView = findViewById(R.id.pager_bottom_tab);
        Fragment indexFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_INDEX).navigation();
        Fragment discoverFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Discover.PAGER_DISCOVERY).navigation();
        Fragment aboutFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.About.PAGER_ABOUT).navigation();
        mFragments = Arrays.asList(indexFragment, discoverFragment, aboutFragment);
        if (indexFragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, indexFragment).commit();
        }
        NavigationController navigationController = pageNavigationView.material()
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
    protected void requestData() {

    }
}
