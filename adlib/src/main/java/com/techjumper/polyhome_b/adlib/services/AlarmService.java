package com.techjumper.polyhome_b.adlib.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.techjumper.corelib.utils.common.JLog;

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
        return super.onStartCommand(intent, flags, startId);
    }
}
