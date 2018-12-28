package com.joe.about;

import android.app.Application;

import com.joe.base.IModuleInit;

/**
 * Created by ATiana on 2018/12/27.
 */
public class AboutModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
