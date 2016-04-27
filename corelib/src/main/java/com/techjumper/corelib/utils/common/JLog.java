package com.techjumper.corelib.utils.common;


import android.text.TextUtils;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JLog {

    public static final String TAG = "HIDETAG";

    public static void d(String str) {
        android.util.Log.d(TAG, "<--  " + str + "  -->");
    }

    public static void d(Throwable e) {
        android.util.Log.d(TAG, "<--  " + e + "  -->");
    }

    public static void e(String str) {
        android.util.Log.e(TAG, "<--  " + str + "  -->");
    }

    public static void showThreadId() {
        showThreadId(null);
    }

    public static void showThreadId(String name) {

        d((TextUtils.isEmpty(name) ? "当前线程ID: " : (name + "线程ID: ")) + Thread.currentThread().getId());
    }
}
