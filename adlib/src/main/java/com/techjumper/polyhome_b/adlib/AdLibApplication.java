package com.techjumper.polyhome_b.adlib;

import android.app.Application;

import com.techjumper.corelib.utils.Utils;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdLibApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.appContext = this;
    }

}
