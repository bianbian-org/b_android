package com.techjumper.polyhomeb.manager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.techjumper.corelib.utils.Utils;

import java.lang.ref.WeakReference;

public class PolyPluginManager {

    public static final String C_PACKAGE_NAME = "com.techjumper.plugin.polyhome";
    private static final String C_MAIN_ACTIVITY = "com.techjumper.polyhome.mvp.v.activity.TabHomeActivity";
    private static final String C_MAIN_ACTION = "com.techjumper.plugin.polyhome.ACTION_MAIN";

    private WeakReference<Activity> mActivityWeakReference;

    private PolyPluginManager(Activity ac) {
        mActivityWeakReference = new WeakReference<Activity>(ac);
    }

    public static PolyPluginManager with(Activity ac) {
        return new PolyPluginManager(ac);
    }

    public boolean contextIsNull() {
        return mActivityWeakReference == null || mActivityWeakReference.get() == null
                || mActivityWeakReference.get().isFinishing();
    }

    public Activity getContext() {
        if (contextIsNull())
            return null;
        return mActivityWeakReference.get();
    }

    public void installCPlugin(String path) {
        String apkPath = "file://" + path;
        Uri uri = Uri.parse(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        getContext().startActivity(intent);
    }

    public boolean isInstallCPlugin() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = Utils.appContext.getPackageManager().getPackageInfo(PolyPluginManager.C_PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return packageInfo != null;
    }

    public void startCMainActivity() {
        Intent intent = new Intent(C_MAIN_ACTION);
        intent.setPackage(C_PACKAGE_NAME);
        getContext().startActivity(intent);
    }

    public void uninstallCPlugin() {
        Uri uri = Uri.parse("package:" + C_PACKAGE_NAME);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        getContext().startActivity(intent);
    }
}