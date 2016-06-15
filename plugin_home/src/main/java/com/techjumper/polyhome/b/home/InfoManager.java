package com.techjumper.polyhome.b.home;

import com.techjumper.corelib.utils.file.PreferenceUtils;

/**
 * Created by kevin on 16/6/15.
 */
public enum InfoManager {

    INSTANCE;

    public static final String KEY_IS_HOME = "key_is_home";

    public static void saveIsHome(boolean isHome) {
        PreferenceUtils.save(KEY_IS_HOME, isHome);
    }

    public static boolean isHome() {
        return PreferenceUtils.get(KEY_IS_HOME, false);
    }
}
