package com.techjumper.polyhome_b.adlib.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.techjumper.corelib.utils.common.JLog;

import java.util.Calendar;
import java.util.TimeZone;

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
        startPollingService(context, triggerAtTime, second, cls, action, true, requestCode);
    }

    public static void startPollingService(Context context, long triggerAtTime, long second
            , Class<?> cls, String action, boolean wakeUp, int requestCode) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        c.setTimeInMillis(triggerAtTime);
        JLog.d("启动了一个定时任务: " + cls.getSimpleName()
                + ", 触发时间(当天的 小时:分)=" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
                + ", 间隔(秒):" + second + ", action=" + action);
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        int type = wakeUp ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC;
        int type = wakeUp ? AlarmManager.RTC : AlarmManager.RTC;
        manager.cancel(pendingIntent);
        manager.setRepeating(type, triggerAtTime,
                second * 1000, pendingIntent);
    }

    public static void startPollingServiceBySet(Context context, long triggerAtTime
            , Class<?> cls, String action, boolean wakeUp, int requestCode, boolean log) {
        startPollingServiceBySet(context, triggerAtTime, cls, action, null, wakeUp, requestCode, log);
    }

    public static void startPollingServiceBySet(Context context, long triggerAtTime
            , Class<?> cls, String action, Bundle extra, boolean wakeUp, int requestCode, boolean log) {
        if (log) {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            c.setTimeInMillis(triggerAtTime);
            JLog.d("启动了一个定时任务: " + cls.getSimpleName()
                    + ", 触发时间(当天的 小时:分)=" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
                    + ", action=" + action);
        }
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        if (extra != null) {
            intent.putExtras(extra);
        }
        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int type = wakeUp ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC;
        manager.cancel(pendingIntent);
//        if (Build.VERSION.SDK_INT >= 19) {
//            manager.setExact(type, triggerAtTime, pendingIntent);
//        } else {
        manager.set(type, triggerAtTime, pendingIntent);
//        }
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