package com.atian.module_base.config;

/**
 * desc: ModuleLifecycleReflexs.java
 * author: Joe
 * created at: 2018/12/27 上午10:16
 * 组件生命周期反射类名管理，在这里注册需要初始化的组件，通过反射动态调用各个组件的初始化方法
 * 注意：以下模块中初始化的Module类不能被混淆
 */

public class ModuleLifecycleReflexs {
    private static final String BaseInit = "com.atian.module_base.base.BaseModuleInit";
    private static final String MainInit = "com.atian.module.main.MainModuleInit";
    private static final String IndexInit = "com.atian.index.IndexModuleInit";

    public static String[] initModuleNames = {BaseInit, MainInit, IndexInit};
}
