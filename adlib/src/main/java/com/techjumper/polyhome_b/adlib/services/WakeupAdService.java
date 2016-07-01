package com.techjumper.polyhome_b.adlib.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.system.PowerUtil;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WakeupAdService extends Service {

    private WakeupAdEvent mEvent = new WakeupAdEvent();

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

        if (isScreenOn()) {
            JLog.d("到了唤醒时间, 但屏幕已经是亮的所以不做操作");
            return super.onStartCommand(intent, flags, startId);
        }

        PowerUtil.wakeUpScreen();
        RxBus.INSTANCE.send(mEvent);
        JLog.d("唤醒屏幕");
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        return Build.VERSION.SDK_INT >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();

    }

    public static class WakeupAdEvent {

    }
}
