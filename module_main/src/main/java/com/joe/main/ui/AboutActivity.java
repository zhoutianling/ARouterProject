package com.joe.main.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.joe.base.bean.User;
import com.joe.main.R;
import com.joe.main.databinding.ActivityAboutBinding;

/**
 * Created by ATiana on 2019/1/4.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        User user = new User();
        user.setName("zhoutianling");
        user.setPwd("abcd");
        binding.setUser(user);
    }
}
