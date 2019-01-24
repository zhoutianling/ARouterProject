package com.joe.main.ui;

import android.os.Bundle;

import com.joe.base.BaseActivity;
import com.joe.base.bean.BaseViewModel;
import com.joe.base.bean.User;
import com.joe.main.R;
import com.joe.main.databinding.ActivityAboutBinding;

/**
 * Created by ATiana on 2019/1/4.
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding, BaseViewModel> {

    @Override
    protected int getToolbarId() {
        return 0;
    }

    @Override
    protected int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_about;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    protected void initView() {
        User user = new User();
        user.setName("textDataBinding");
        user.setPwd("abcd");
        binding.setUser(user);
    }
}
