package com.techjumper.polyhomeb.manager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.net.NetExecutor;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.user.UserManager;

import java.lang.ref.WeakReference;

import rx.Observable;

public class PolyPluginManager {

    public static final String C_PACKAGE_NAME = "com.techjumper.plugin.polyhome";
    private static final String C_MAIN_ACTIVITY = "com.techjumper.polyhome.mvp.v.activity.TabHomeActivity";
    private static final String C_MAIN_ACTION = "com.techjumper.plugin.polyhome.ACTION_MAIN";

    public static final String KEY_FAMILY_INFO = "key_family_info";
    public static final String KEY_IS_B_PLUGIN = "key_is_b_plugin";
    public static final String KEY_ID = "key_id";
    public static final String KEY_PHONE_NUMBER = "key_phone_number";
    public static final String KEY_TICKET = "key_ticket";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_AVATAR = "key_avatar";
    public static final String KEY_CURRENT_FAMILY_ID = "key_current_family_id";
    public static final String KEY_CURRENT_FAMILY_NAME = "key_current_family_name";

    private WeakReference<Activity> mActivityWeakReference;

    private PolyPluginManager(Activity ac) {
        mActivityWeakReference = new WeakReference<>(ac);
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

    private void startCMainActivity(String familyInfo) {
        Intent intent = new Intent(C_MAIN_ACTION);
        intent.setPackage(C_PACKAGE_NAME);
        intent.putExtra(KEY_FAMILY_INFO, familyInfo);
        intent.putExtra(KEY_IS_B_PLUGIN, true);
        intent.putExtra(KEY_ID, UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));
        intent.putExtra(KEY_PHONE_NUMBER, UserManager.INSTANCE.getUserInfo(UserManager.KEY_PHONE_NUMBER));
        intent.putExtra(KEY_TICKET, UserManager.INSTANCE.getTicket());
        intent.putExtra(KEY_USER_NAME, UserManager.INSTANCE.getUserInfo(UserManager.KEY_USER_NAME));
        intent.putExtra(KEY_AVATAR, UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR));
        intent.putExtra(KEY_CURRENT_FAMILY_ID, UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID));
        intent.putExtra(KEY_CURRENT_FAMILY_NAME, UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_SHOW_TITLE_NAME));
        getContext().startActivity(intent);
    }

    public void uninstallCPlugin() {
        Uri uri = Uri.parse("package:" + C_PACKAGE_NAME);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        getContext().startActivity(intent);
    }

    public Observable<Boolean> startCMainActivityAuto() {
        return PolyPluginFileManager.getInstance().getFamilyInfoFromLocal()
                .flatMap(s -> {
                    if (TextUtils.isEmpty(s)) {
                        JLog.d("从网络获取家庭信息");
                        return fetchFamilyInfoFromNet();
                    }
                    JLog.d("从本地获取家庭信息");
                    return Observable.just(s);
                })
                .map(s -> {
                    JLog.d("家庭信息：" + s);
                    if (TextUtils.isEmpty(s))
                        return false;
                    startCMainActivity(s);
                    return true;
                });
    }

    public Observable<String> fetchFamilyInfoFromNet() {
        return NetExecutor.getInstance().queryFamilyInfo()
                .flatMap(queryFamilyEntity -> {
                    if (!NetHelper.isSuccess(queryFamilyEntity))
                        return Observable.error(new Exception(Utils.appContext.getString(R.string.no_specified_family_information)));
                    if (queryFamilyEntity.getData() == null
                            || TextUtils.isEmpty(queryFamilyEntity.getData().getId()))
                        return Observable.error(new Exception(Utils.appContext.getString(R.string.no_specified_family_information)));
                    return PolyPluginFileManager.getInstance().saveFamilyInfoToLocal(queryFamilyEntity);
                })
                .flatMap(aBoolean -> PolyPluginFileManager.getInstance().getFamilyInfoFromLocal());
    }

}