package com.joe.about.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.joe.about.ui.AboutFragment;
import com.joe.base.R;

/**
 * Created by ATiana on 2018/12/27.
 */
public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new AboutFragment()).commit();
    }
}
