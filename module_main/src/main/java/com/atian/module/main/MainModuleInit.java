package com.atian.module.main;

import android.app.Application;

import com.atian.module_base.base.IModuleInit;

/**
 * desc: MainModuleInit.java
 * author: Joe
 * created at: 2018/12/27 上午10:21
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
