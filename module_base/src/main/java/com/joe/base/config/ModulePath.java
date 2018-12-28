package com.joe.base.config;

/**
 * desc: ModulePath.java
 * author: Joe
 * created at: 2018/12/27 上午10:16
 * 通过反射动态调用各个组件的初始化方法
 * Module类不能被混淆
 */

public class ModulePath {
    private static final String BaseInit = "com.joe.base.BaseModuleInit";
    private static final String MainInit = "com.joe.main.MainModuleInit";
    private static final String IndexInit = "com.joe.index.IndexModuleInit";
    private static final String DiscoverInit = "com.joe.discovery.DiscoveryModuleInit";
    private static final String AboutInit = "com.joe.about.AboutModuleInit";

    public static String[] initModuleNames = {BaseInit, MainInit, IndexInit, DiscoverInit, AboutInit};
}
