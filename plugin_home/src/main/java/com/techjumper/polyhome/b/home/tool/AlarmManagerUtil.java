package com.techjumper.polyhome.b.home.tool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

    public static void setWeatherTime(Context context, int hour, int minute) {

        //如果minute等于0，设置为1分钟
        if (minute <= 0) {
            minute = 1;
        }

        Log.d(TAG, "Weather: hour:" + hour + " minute:" + minute);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.WEATHER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.WEATHER, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, pendingIntent);
    }

    public static void setNoticeTime(Context context, int hour, int minute) {
        //如果minute等于0，设置为1分钟
        if (minute <= 0) {
            minute = 1;
        }

        Log.d(TAG, "Notice: hour:" + hour + " minute:" + minute);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra(AlarmReceiver.TYPE, AlarmReceiver.NOTICES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.NOTICES, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, pendingIntent);
    }
}
