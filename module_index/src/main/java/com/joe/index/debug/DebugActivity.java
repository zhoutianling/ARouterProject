package com.joe.index.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.joe.index.R;
import com.joe.index.ui.IndexFragment;


public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new IndexFragment()).commit();
    }
}
