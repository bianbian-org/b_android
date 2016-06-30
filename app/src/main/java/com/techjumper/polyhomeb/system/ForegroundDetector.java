package com.techjumper.polyhomeb.system;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/5/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

/**
 * 判断应用是否回到前台或隐藏
 */
public class ForegroundDetector implements Application.ActivityLifecycleCallbacks {

    private static final String PREF = "LockerPref";
    private static final String IS_APP_LEAVED = "isAppLeaved";

    private static int started;
    private static int stopped;
    public static int sForegroundCount;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;

        //  check if activity start from background state


        SharedPreferences pref = activity.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        boolean isAppLeaved = pref.getBoolean(IS_APP_LEAVED, false);
//        Log.d("HIDETAG", activity.getClass().getSimpleName() + " onActivityStarted(): started=" + started + " isAppLeaved=" + isAppLeaved);
        if (isAppLeaved) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(IS_APP_LEAVED, false);
            editor.commit();

            sForegroundCount++;
            //每次切到前台就重连
            //TODO do somthing
        }

    }


    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
//        Log.d("HIDETAG", activity.getClass().getSimpleName() + " onActivityStopped(): stopted=" + stopped + " visible=" + isApplicationVisible());
        if (!isApplicationVisible()) {
            // here you store the application background state in preferences
            SharedPreferences pref = activity.getSharedPreferences(PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(IS_APP_LEAVED, true);
            editor.commit();


            //TODO do somthing
        }
    }


    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
