package com.techjumper.polyhome.b.home.tool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by kevin on 16/6/21.
 */
public class AlarmManagerUtil {
    private static final String TAG = "AlarmManagerUtil";

    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h

    public static void setTime(Context context, int hour, int minute) {
        //如果minute等于0，设置为1分钟
        if (minute <= 0) {
            minute = 1;
        }
        Log.d(TAG, "hour:" + hour + " minute:" + minute);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, pendingIntent);
    }
}
