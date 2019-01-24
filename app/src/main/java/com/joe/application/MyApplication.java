package com.joe.application;

import com.joe.base.BaseApplication;
import com.joe.base.config.ModuleConfig;
import com.joe.common.crash.CrashHandler;
import com.joe.common.utils.Utils;

/**
 * desc: MyApplication.java
 * author: Joe
 * created at: 2018/12/27 上午10:53
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);//初始化工具类中上下文
        CrashHandler.init(this);//初始化全局异常捕获
        //初始化组件(靠前)
        ModuleConfig.getInstance().initModuleAhead(this);
        //....
        //初始化组件(靠后)
        ModuleConfig.getInstance().initModuleLow(this);
    }
}
