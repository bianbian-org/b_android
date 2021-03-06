package com.techjumper.polyhomeb.system;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.anrwatchdog.ANRWatchDog;
import com.techjumper.lib2.others.Lib2Application;
import com.techjumper.polyhomeb.BuildConfig;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.umeng.analytics.MobclickAgent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PolyApplication extends Lib2Application {

    //判断应用是否回到前台或隐藏
    private ForegroundDetector mForegroundDetector = new ForegroundDetector();

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        if (BuildConfig.DEBUG)
            new ANRWatchDog().start();
        registerActivityLifecycleCallbacks(mForegroundDetector);
//        if (BuildConfig.DEBUG) {
//            JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        }
//        JPushInterface.init(this);            // 初始化 JPush
        com.techjumper.lib2.Config.sIsDebug = BuildConfig.DEBUG; //控制lib2是否开启 debug ( debug 模式下会打印 http-log 等等)
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCrash(Throwable ex) {
//        友盟默认自动拦截错误, 如果不注释这句代码会导致错误重复
//        MobclickAgent.reportError(this, ex);
        MobclickAgent.onKillProcess(this);
    }

    @Override
    public String[] fetchCrashFolderName() {
        return new String[]{Config.sParentDirName, Config.sLogDirName};
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
    protected String getMedicalBaseUrl() {
        return Config.sMedicalUrl;
    }

    @Override
    protected String getCAPPBaseUrl() {
        return Config.sCBaseUrl;
    }

    @Override
    protected Class getDefaultInterfaceClass() {
        return ServiceAPI.class;
    }

}
