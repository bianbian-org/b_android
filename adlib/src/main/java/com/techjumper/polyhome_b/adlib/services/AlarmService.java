package com.techjumper.polyhome_b.adlib.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AlarmService extends Service {

    private AlarmServiceEvent mEvent = new AlarmServiceEvent();

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
        JLog.d("AlarmService发送RxBus");
        RxBus.INSTANCE.send(mEvent);
        return super.onStartCommand(intent, flags, startId);
    }

    public static class AlarmServiceEvent {

    }
}
