package com.techjumper.polyhomeb.utils;

import com.techjumper.corelib.utils.media.SoundManager;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SoundUtils {

    public static int KEY_SCAN = 1;
    public static final String PATH_SCAN = "sound/di.wav";

    public static void playScanSound() {
        SoundManager.getInstance().play(KEY_SCAN);
    }

    public static void initSoundManager() {
        SoundManager sm = SoundManager.getInstance();
        if (!sm.hasKey(KEY_SCAN)) {
            sm.load(KEY_SCAN, PATH_SCAN);
        }
    }
}
