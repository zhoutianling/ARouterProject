package com.joe.about.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.joe.about.R;
import com.joe.base.router.RouterFragmentPath;

@Route(path = RouterFragmentPath.About.PAGER_ABOUT_FEEDBACK)
public class FeedBackActivity extends AppCompatActivity {
    @Autowired(name = "userName")
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_feed_back);
        TextView userName = findViewById(R.id.userName);
        ARouter.getInstance().inject(this);
        userName.setText(name);
    }
}
