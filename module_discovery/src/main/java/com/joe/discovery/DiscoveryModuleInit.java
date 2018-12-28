package com.joe.discovery;

import android.app.Application;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.joe.base.IModuleInit;
import com.joe.base.router.RouterFragmentPath;

/**
 * Created by ATiana on 2018/12/28.
 */
public class DiscoveryModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
