package com.joe.main;

import android.app.Application;

import com.joe.base.IModuleInit;

/**
 * desc: MainModuleInit.java
 * author: Joe
 * created at: 2018/12/27 上午10:49
 */
public class MainModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
