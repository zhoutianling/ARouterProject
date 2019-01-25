package com.joe.main.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.joe.base.BaseActivity;
import com.joe.base.adapter.MyPagerAdapter;
import com.joe.base.bean.BaseViewModel;
import com.joe.base.router.RouterActivityPath;
import com.joe.base.router.RouterFragmentPath;
import com.joe.common.widget.coordinatortablayout.CoordinatorTabLayout;
import com.joe.common.widget.dialog.MessageDialog;
import com.joe.main.R;
import com.joe.main.databinding.MainActivityBinding;

import java.util.Arrays;
import java.util.List;

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends BaseActivity<MainActivityBinding, BaseViewModel> implements NavigationView.OnNavigationItemSelectedListener {
    private List<Fragment> mFragments;
    private ViewPager mViewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RelativeLayout drawerHeader;
    private int[] mColorArray;
    private CoordinatorTabLayout coordinatorTabLayout;
    private final String[] mTitles = {"首页", "发现", "关于", "更多"};


    @Override
    protected void initView() {
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};
        coordinatorTabLayout = $(R.id.coordinatorTablayout);

        navigationView = $(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//显示图标原有颜色
        mViewPager = $(R.id.view_pager);

        drawerLayout = $(R.id.drawer_layout);
        drawerHeader = $(R.id.rl_header);
        drawerHeader.setBackgroundResource(R.mipmap.header_material);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        setBlueBg(drawerHeader);

        Fragment indexFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_INDEX).navigation();
        Fragment discoverFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Discover.PAGER_DISCOVERY).navigation();
        Fragment aboutFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.About.PAGER_ABOUT).navigation();
        Fragment moreFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.About.PAGER_ABOUT).navigation();
        mFragments = Arrays.asList(indexFragment, discoverFragment, aboutFragment, moreFragment);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));

        coordinatorTabLayout.setTranslucentStatusBar(this)
                .setContentScrimColorArray(mColorArray)
                .setBackEnable(true)
                .setBackEnable(false)
                .setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_quit) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.item_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (i == R.id.item_cooperation) {
            ARouter.getInstance().build(RouterFragmentPath.About.PAGER_ABOUT_FEEDBACK)
                    .withString("userName", "zhoutianling")
                    .navigation(MainActivity.this);
        }
        drawerLayout.closeDrawer(Gravity.START);
        return true;
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

    @Override
    protected int getToolbarId() {
        return 0;
    }

    @Override
    protected int initContentView(Bundle savedInstanceState) {
        return R.layout.main_activity;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

}
