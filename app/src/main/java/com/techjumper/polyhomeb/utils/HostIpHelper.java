package com.techjumper.polyhomeb.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.polyhomeb.BuildConfig;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HostIpHelper {

    public static final String PREFERENCE_NAME = "hostip";
    public static final String KEY_FAMILY_PREFIX = "family_";

    private static volatile HostIpHelper sSelf;


    public static HostIpHelper getInstance() {
        if (sSelf == null) {
            synchronized (HostIpHelper.class) {
                if (sSelf == null) {
                    sSelf = new HostIpHelper();
                }
            }
        }
        return sSelf;
    }

    public void clear() {
        getPreference().edit().clear().commit();
    }

    public void saveHostIpToFamily(String ip) {
//        ToastUtils.showLong("保存主机IP: " + ip);
        if (ip == null) ip = "";
        String key = getKeyByCurrentFamilyId();
        if (TextUtils.isEmpty(key)) return;
        getPreference().edit().putString(key, ip).commit();
    }

    private SharedPreferences getPreference() {
        return PreferenceUtils.getPreference(PREFERENCE_NAME);
    }

    public String getFamilyHostIp() {
        String key = getKeyByCurrentFamilyId();
        if (TextUtils.isEmpty(key)) return "";
        return getPreference().getString(key, "");
    }

    public boolean hostIpExist() {
        String hostIp = getFamilyHostIp();
        return !TextUtils.isEmpty(hostIp);
    }

    private String getKeyByCurrentFamilyId() {
//        String familyId = getCurrentFamilyId();
        String familyId = "";   //此处是为了让编译通过,特地这么写的,因为类中76行以下被注释了,注释的原因是KEY_CURRENT_FAMILY_ID没有这个字段了.
        if (TextUtils.isEmpty(familyId)) return "";
        if (BuildConfig.DEBUG) {

//            getPreference().edit().putString(KEY_FAMILY_PREFIX + familyId, "192.168.199.228").commit();

//            getPreference().edit().putString(KEY_FAMILY_PREFIX + familyId, "10.0.1.28").commit();

        }

        return KEY_FAMILY_PREFIX + familyId;
    }

//    private String getCurrentFamilyId() {
//        return UserManager.INSTANCE.getCurrentFamilyInfo(UserManager.KEY_CURRENT_FAMILY_ID);
//    }

}
