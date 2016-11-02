package com.techjumper.polyhome.b.home.tool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.home.tool.alarm.AlarmReceiver;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by kevin on 16/6/21.
 */
public class AlarmManagerUtil {
    public static final String TAG = "AlarmManagerUtil";

    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    private static final int NOTICES_TIME = (1000 * 60 * 60 * 3) / 4;// 45min

    public static void setWeatherTime(Context context) {

        int hour = CommonDateUtil.getHour();
        int minute = CommonDateUtil.getMinute();
        int triggerAtMillis = 1000 * 60 * 60 * 2;

        Log.d(TAG, "Weather: hour:" + hour + " minute:" + minute);
        Log.d("tianqi", "Weather: hour:" + hour + " minute:" + minute);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.WEATHER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.WEATHER, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + triggerAtMillis, pendingIntent);
    }

    public static void setNoticeTime(Context context) {
        int hour = CommonDateUtil.getHour();
        int minute = CommonDateUtil.getMinute();
        int delayTime = 45 + new Random().nextInt(30);
        int triggerAtMillis = delayTime * 1000 * 60;

        Log.d(TAG, "Notice: hour:" + hour + " minute:" + minute + "delayTime:" + delayTime);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.NOTICES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.NOTICES, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + triggerAtMillis, pendingIntent);
    }

    public static void setAdClick(Context context) {
        int hour = CommonDateUtil.getHour();
        int minute = CommonDateUtil.getMinute();
//        int triggerAtMillis = 1000 * 60 * 60;
        int triggerAtMillis = 1000 * 60 * 5;

        Log.d(TAG, "Notice: hour:" + hour + " minute:" + minute + "triggerAtMillis:" + triggerAtMillis);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.ADCLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.ADCLICK, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + triggerAtMillis, pendingIntent);
    }

    public static void setSubmitOnlineClick(Context context) {
        int hour = CommonDateUtil.getHour();
        int minute = CommonDateUtil.getMinute();
        int triggerAtMillis = 1000 * 60 * 2;

        Log.d(TAG, "SubmitOnline: hour:haha" + hour + " minute:" + minute + "triggerAtMillis:" + triggerAtMillis + "  PackageName:" + context.getPackageName());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.SUBMITONLINE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.SUBMITONLINE, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + triggerAtMillis, pendingIntent);
    }
}
