package com.joe.discovery.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.joe.discovery.ui.DiscoveryFragment;

/**
 * Created by ATiana on 2018/12/28.
 */
public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.joe.base.R.layout.base_activity_layout);
        getSupportFragmentManager().beginTransaction().add(com.joe.base.R.id.container, new DiscoveryFragment()).commit();
    }
}
