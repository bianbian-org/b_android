package com.techjumper.polyhomeb.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.manager.UpdateManager;
import com.techjumper.polyhomeb.net.ServiceAPI;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UpdateService extends Service {

    private Notification notification;
    private NotificationManager manager;
    private Subscription mSubs1;

    private static final int NOTIFICATION_ID = 1;
    public static final String KEY_URL = "url";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.download_begin))
                .setWhen(System.currentTimeMillis())
                .setContent(new RemoteViews(getPackageName(), R.layout.layout_download));
        notification = builder.build();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);

        mSubs1 = RetrofitHelper
                .<ServiceAPI>createDefault()
                .downloadNewApk(intent.getStringExtra(KEY_URL))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
//                        RxUtils.unsubscribeIfNotNull(mSubs1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        JLog.e(e.toString());
//                        RxUtils.unsubscribeIfNotNull(mSubs1);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
//                        if (responseBody == null || responseBody.byteStream() == null) {
//                            ToastUtils.show(getString(R.string.download_error));
//                            return;
//                        }

                        UpdateManager.writeFile2Disk(responseBody, new UpdateManager.ICurrentProgress() {

                            @Override
                            public void progressDatas(String percent) {
                                notification.contentView.setProgressBar(R.id.pb, 100, NumberUtil.convertToint(percent, 0), false);
                                String progress = String.format(getString(R.string.current_progress), percent);
                                notification.contentView.setTextViewText(R.id.tv_progress, progress);
                                manager.notify(NOTIFICATION_ID, notification);

                                if (NumberUtil.convertToint(percent, 0) == 100) {
                                    notification.contentView.setProgressBar(R.id.pb, 100, 100, false);
                                    notification.contentView.setTextViewText(R.id.tv_progress, getString(R.string.download_complete));
                                    manager.notify(NOTIFICATION_ID, notification);
                                }
                            }

                            @Override
                            public void onDownloadError() {
                                ToastUtils.show(getString(R.string.download_error));
//                                stopForeground(true);
                            }
                        });
                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        RxUtils.unsubscribeIfNotNull(mSubs1);
        super.onDestroy();
    }
}
