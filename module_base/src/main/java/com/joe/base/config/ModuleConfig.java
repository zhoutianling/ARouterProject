package com.joe.base.config;

import android.app.Application;
import android.util.Log;

import com.joe.base.IModuleInit;

import io.reactivex.annotations.Nullable;

/**
 * desc: ModuleConfig.java
 * author: Joe
 * created at: 2018/12/27 下午4:18
 * 作为组件生命周期初始化的配置类，通过反射机制，动态调用每个组件初始化逻辑
 */
public class ModuleConfig {
    //内部类，在装载该内部类时才会去创建单例对象
    private static class SingletonHolder {
        public static ModuleConfig instance = new ModuleConfig();
    }

    public static ModuleConfig getInstance() {
        return SingletonHolder.instance;
    }

    private ModuleConfig() {
    }

    //初始化组件-靠前
    public void initModuleAhead(@Nullable Application application) {
        for (String moduleInitName : ModulePath.initModuleNames) {
            try {
                Class<?> clazz = Class.forName(moduleInitName);
                Log.i("zzz", "className:" + clazz.getSimpleName());
                IModuleInit init = (IModuleInit) clazz.newInstance();
                //调用初始化方法
                init.onInitAhead(application);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //初始化组件-靠后
    public void initModuleLow(@Nullable Application application) {
        for (String moduleInitName : ModulePath.initModuleNames) {
            try {
                Class<?> clazz = Class.forName(moduleInitName);
                IModuleInit init = (IModuleInit) clazz.newInstance();
                //调用初始化方法
                init.onInitLow(application);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
