package com.techjumper.polyhome.b.info;

import com.techjumper.lib2.others.Lib2Application;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.info.net.ServiceAPI;

/**
 * Created by kevin on 16/4/28.
 */
public class InfoApplication extends Lib2Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PluginEngine.getInstance().init(this);
    }

    @Override
    protected int getDbVersion() {
        return Config.DEFAULT_DB_VERSION;
    }

    @Override
    protected String getDefaultBaseUrl() {
        return Config.sBaseUrl;
    }

    @Override
    protected Class getDefaultInterfaceClass() {
        return ServiceAPI.class;
    }

    @Override
    public void onCrash(Throwable ex) {

    }

    @Override
    public String[] fetchCrashFolderName() {
        return new String[]{"polyhome_home", "log"};
    }
}
