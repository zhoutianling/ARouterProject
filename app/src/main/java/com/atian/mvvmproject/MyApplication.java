package com.atian.mvvmproject;

import com.atian.module_base.config.ModuleLifecycleConfig;

import me.goldze.mvvmhabit.base.BaseApplication;

/**
 * Created by ATiana on 2018/12/26.
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
        //....
        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);
    }
}
