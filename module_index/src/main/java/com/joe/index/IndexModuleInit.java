package com.joe.index;

import android.app.Application;

import com.joe.base.IModuleInit;

/**
 * desc: IndexModuleInit.java
 * author: Joe
 * created at: 2018/12/27 上午10:16
 */
public class IndexModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
