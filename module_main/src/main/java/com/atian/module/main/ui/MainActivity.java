package com.atian.module.main.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.atian.module.main.R;
import com.atian.module_base.router.RouterActivityPath;
import com.atian.module_base.router.RouterFragmentPath;

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment indexFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_INDEX).navigation();
        if (indexFragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, indexFragment).commit();
        }
    }
}
