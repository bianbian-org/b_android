package com.techjumper.polyhome;

import com.techjumper.lib2.others.Lib2Application;
import com.techjumper.polyhome.Config;
import com.techjumper.polyhome.net.ServiceAPI;

/**
 * Created by kevin on 16/5/9.
 */
public class SettingApplication extends Lib2Application {
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
