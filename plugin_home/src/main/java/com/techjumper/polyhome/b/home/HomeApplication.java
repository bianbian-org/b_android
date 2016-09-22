package com.techjumper.polyhome.b.home;

import android.util.Log;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.lib2.others.Lib2Application;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.net.ServiceAPI;

/**
 * Created by kevin on 16/4/28.
 */
public class HomeApplication extends Lib2Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PluginEngine.getInstance().init(this);
        Utils.appContext = this;

        /**
         * 将插件列表保存到BHost
         */
//        Observable
//                .create(subscriber -> {
//                    try {
//                        String data = FileUtils.loadInputStreamToString(getAssets().open(Config.PLUGIN_LIST_ASSET_PATH));
//                        HashMap<String, String> dataMap = new HashMap<>();
//                        dataMap.put(Config.PLUGIN_LIST_ASSET_NAME, data);
//                        PluginEngineUtil.saveData(Config.PLUGIN_LIST_ASSET_NAME, dataMap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        JLog.e(e);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .subscribe();
    }

    @Override
    protected int getDbVersion() {
        return Config.DEFAULT_DB_VERSION;
    }

    @Override
    protected String getDefaultBaseUrl() {
        Log.d("hehe", Config.sBaseUrl);
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
