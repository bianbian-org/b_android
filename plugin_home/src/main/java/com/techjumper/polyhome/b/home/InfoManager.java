package com.techjumper.polyhome.b.home;

import android.util.Log;

import com.techjumper.corelib.utils.file.PreferenceUtils;

/**
 * Created by kevin on 16/6/15.
 */
public enum InfoManager {

    INSTANCE;

    public static final String KEY_IS_HOME = "key_is_home";
    public static final String KEY_USERINFO_FILE = "key_userinfo_file";
    public static final String KEY_HEARTBEAT_DATE = "key_heartbeat_date";

    public static void saveIsHome(boolean isHome) {
        PreferenceUtils.save(KEY_IS_HOME, isHome);
    }

    public static boolean isHome() {
        return PreferenceUtils.get(KEY_IS_HOME, false);
    }

    /**
     * 保存文件名
     *
     * @param fileName
     */
    public void saveUserInfoFile(String fileName) {
        PreferenceUtils.save(KEY_USERINFO_FILE, fileName);
    }

    public String getUserInfoFile() {
        return PreferenceUtils.get(KEY_USERINFO_FILE, "");
    }

    public void saveHeartbeatDate(long time) {
        Log.d("heartBeattime", "保存time" + time);
        PreferenceUtils.save(KEY_HEARTBEAT_DATE, time);
    }

    public long getHeartbeatDate() {
        Log.d("heartBeattime", "获取time" + PreferenceUtils.get(KEY_HEARTBEAT_DATE, 0L));
        return PreferenceUtils.get(KEY_HEARTBEAT_DATE, 0L);
    }
}
