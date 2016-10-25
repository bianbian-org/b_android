package com.techjumper.polyhomeb.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.event.ScanDeviceEvent;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ScanBluetoothService extends Service {

    private Subscription mSubs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSubs = Observable.interval(15, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        RxUtils.unsubscribeIfNotNull(mSubs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        //10S一次，扫描周围设备
                        RxBus.INSTANCE.send(new ScanDeviceEvent());
                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        RxUtils.unsubscribeIfNotNull(mSubs);
        super.onDestroy();
    }
}
