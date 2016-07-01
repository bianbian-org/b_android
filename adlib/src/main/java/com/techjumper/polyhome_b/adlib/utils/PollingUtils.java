package com.techjumper.polyhome_b.adlib.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.techjumper.corelib.utils.common.JLog;

public class PollingUtils {


    public static void startPollingService(Context context, boolean immediately, long seconds, Class<?> cls, String action) {
        JLog.d("启动了一个定时任务; action=" + action);
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = SystemClock.elapsedRealtime();
        if (!immediately) {
            triggerAtTime += seconds * 1000;
        }
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,
                seconds * 1000, pendingIntent);
    }

    public static void startPollingService(Context context, long triggerAtTime, long second
            , Class<?> cls, String action, int requestCode) {
        JLog.d("启动了一个定时任务; 触发时间=" + triggerAtTime + ", 间隔:" + second + ", action=" + action);
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,
                second * 1000, pendingIntent);
    }

    public static void stopPollingService(Context context, Class<?> cls, String action, int code) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, code,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
}