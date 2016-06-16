package com.techjumper.lib2.manager;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.receivers.RxBroadcastReceiver;
import com.f2prateek.rx.receivers.wifi.NetworkStateChangedEvent;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.system.AppUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.f2prateek.rx.receivers.internal.Preconditions.checkNotNull;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/4/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NetworkManager {
    private static NetworkManager INSTANCE;

    private NetworkManager() {
    }

    private Observable<NetworkStateChangedEvent> mObservable;

    public static NetworkManager getInstance() {
        if (INSTANCE == null) {
            synchronized (NetworkManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetworkManager();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<NetworkStateChangedEvent> networkChanges() {
        if (mObservable != null) return mObservable;
        Observable<NetworkStateChangedEvent> observable =
                Observable
                        .create((Observable.OnSubscribe<NetworkStateChangedEvent>) subscriber -> {
                            NetworkStateChangedEvent event = new NetworkStateChangedEvent() {
                                @NonNull
                                @Override
                                public NetworkInfo networkInfo() {
                                    return AppUtils.getActiveNetworkInfo();
                                }

                                @Nullable
                                @Override
                                public String bssid() {
                                    return "";
                                }

                                @Nullable
                                @Override
                                public WifiInfo wifiInfo() {
                                    return AppUtils.getWifiInfo();
                                }
                            };
                            subscriber.onNext(event);
                            subscriber.onCompleted();
                        });


        mObservable = Observable.merge(observable, networkStateChanges(Utils.appContext))
                .observeOn(AndroidSchedulers.mainThread());
        return mObservable;
    }

    @CheckResult
    @NonNull //
    public static Observable<NetworkStateChangedEvent> //
    networkStateChanges(@NonNull final Context context) {
        checkNotNull(context, "context == null");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return RxBroadcastReceiver.create(context, filter)
                .map(intent -> {
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
                    WifiInfo wifiInfo = AppUtils.getWifiInfo();
                    return NetworkStateChangedEvent.create(networkInfo, bssid, wifiInfo);
                });
    }


}
