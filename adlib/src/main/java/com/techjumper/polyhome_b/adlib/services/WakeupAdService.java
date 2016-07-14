package com.techjumper.polyhome_b.adlib.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
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
public class WakeupAdService extends Service {

    public static final String ACTION_WAKE_UP = "action_wake_up";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JLog.d("开启唤醒广告服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendBroadcast(new Intent(ACTION_WAKE_UP));
        PollingUtils.startPollingServiceBySet(Utils.appContext
                , getTriggerTime(), WakeupAdService.class, "", true, AdController.CODE_WAKEUP_ALARM, true);
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

    @SuppressLint("NewApi")
    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) Utils.appContext.getSystemService(Context.POWER_SERVICE);
        return Build.VERSION.SDK_INT >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();

    }

}
