package com.trade.main;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by user on 1/23/2018.
 */

@Database(name = AppController.NAME, version = AppController.VERSION)
public class AppController extends Application {

    public static final String NAME = "Trade";

    public static final int VERSION =1;

    @Override
    public void onCreate() {

        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
