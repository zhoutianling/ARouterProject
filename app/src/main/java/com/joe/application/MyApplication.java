package com.joe.application;

import com.joe.base.config.ModuleConfig;

import me.goldze.mvvmhabit.base.BaseApplication;

/**
 * desc: MyApplication.java
 * author: Joe
 * created at: 2018/12/27 上午10:53
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化组件(靠前)
        ModuleConfig.getInstance().initModuleAhead(this);
        //....
        //初始化组件(靠后)
        ModuleConfig.getInstance().initModuleLow(this);
    }
}
