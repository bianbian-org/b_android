package com.techjumper.polyhomeb.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxCountdown;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.event.CountdownEvent;

import rx.Subscriber;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CountdownService extends Service {

    public static final String KEY_COUNTDOWN = "key_countdown";
    private static int sDefaultCount = 60;

    private static CountdownEvent mEvent;

    private Subscription mCountdownSub;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mEvent != null) return super.onStartCommand(intent, flags, startId);
        int count = getCount(intent);

        mEvent = new CountdownEvent();
        mCountdownSub = RxCountdown.countdown(count)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mEvent = null;
                        RxUtils.unsubscribeIfNotNull(mCountdownSub);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mEvent.setCount(integer);
                        RxBus.INSTANCE.send(mEvent);
                    }
                });
        return super.onStartCommand(intent, flags, startId);
    }

    private int getCount(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return sDefaultCount;
        }
        Bundle extras = intent.getExtras();
        return extras.getInt(KEY_COUNTDOWN, sDefaultCount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEvent = null;
        RxUtils.unsubscribeIfNotNull(mCountdownSub);
    }

    public static boolean isCountingDown() {
        return mEvent != null;
    }

    public static int getCurrCount() {
        return mEvent.getCount();
    }
}
