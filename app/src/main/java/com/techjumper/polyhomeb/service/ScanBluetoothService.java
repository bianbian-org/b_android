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
    private boolean mIsServiceAlive = false;  //服务可尚存？今安在？

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mIsServiceAlive) return super.onStartCommand(intent, flags, startId);
        mIsServiceAlive = true;
        mSubs = Observable.interval(10, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
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
        mIsServiceAlive = false;
        RxUtils.unsubscribeIfNotNull(mSubs);
        super.onDestroy();
    }
}
