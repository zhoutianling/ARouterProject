package com.joe.main.ui;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.joe.base.BaseActivity;
import com.joe.base.adapter.MyPagerAdapter;
import com.joe.base.router.RouterActivityPath;
import com.joe.base.router.RouterFragmentPath;
import com.joe.commom_library.widget.dialog.MessageDialog;
import com.joe.main.R;

import java.util.Arrays;
import java.util.List;

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<Fragment> mFragments;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RelativeLayout drawerHeader;

    private final String[] mTitles = {"首页", "发现", "关于"};

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//显示图标原有颜色
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerHeader = findViewById(R.id.rl_header);
        drawerHeader.setBackgroundResource(R.mipmap.header_material);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        setBlueBg(drawerHeader);
        Fragment indexFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_INDEX).navigation();
        Fragment discoverFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Discover.PAGER_DISCOVERY).navigation();
        Fragment aboutFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.About.PAGER_ABOUT).navigation();
        mFragments = Arrays.asList(indexFragment, discoverFragment, aboutFragment);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    @Override
    protected void requestData() {
        showLoadingDialog("加载中...");
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissDialog();
            }
        }, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        int i = item.getItemId();
        if (i == R.id.action_about) {
        } else if (i == R.id.action_about_me) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        new MessageDialog.Builder(this)
                .setTitle("提示")
                .setMessage("欢迎下次再来")
                .setConfirm("确定")
                .setCancel("取消").setListener(new MessageDialog.OnListener() {
            @Override
            public void confirm(Dialog dialog) {
                MainActivity.this.finish();
            }

            @Override
            public void cancel(Dialog dialog) {
            }
        }).show();
    }
}
