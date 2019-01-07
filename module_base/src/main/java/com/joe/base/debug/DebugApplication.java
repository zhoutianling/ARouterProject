package com.joe.base.debug;


import com.joe.base.BaseApplication;
import com.joe.base.config.ModuleConfig;



public class DebugApplication extends BaseApplication {
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
