package com.techjumper.polyhome_b.adlib.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhome_b.adlib.manager.AdController;
import com.techjumper.polyhome_b.adlib.utils.PollingUtils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AlarmService extends Service {

    public static final String ACTION_ALARM_SERVICE = "action_alarm_service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JLog.d("开启AlarmService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendBroadcast(new Intent(ACTION_ALARM_SERVICE));
        PollingUtils.startPollingServiceBySet(Utils.appContext
                , getTriggerTime(), AlarmService.class, "", true, AdController.CODE_ALARM_SERVICE, true);
        return super.onStartCommand(intent, flags, startId);
    }

    private long getTriggerTime() {
        Calendar c = Calendar.getInstance();
        long oneHourLater = System.currentTimeMillis() + 1000L * 60 * 60;
//        long oneHourLater = System.currentTimeMillis() + 1000L * 5;
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        c.setTimeInMillis(oneHourLater);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
//        return oneHourLater;
        return c.getTimeInMillis();
    }
}
