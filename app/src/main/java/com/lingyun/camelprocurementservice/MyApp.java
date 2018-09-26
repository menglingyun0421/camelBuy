package com.lingyun.camelprocurementservice;

import android.app.Application;

import com.mob.MobApplication;
import com.mob.MobSDK;

/**
 * Created by 凌云 on 2018/9/6.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MobSDK.init(this);
    }
}
