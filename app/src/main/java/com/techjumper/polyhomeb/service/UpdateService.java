package com.techjumper.polyhomeb.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.downloader.SimpleDownloadBuilder;
import com.techjumper.lib2.downloader.listener.IDownloadError;
import com.techjumper.lib2.downloader.listener.IDownloadProgress;
import com.techjumper.lib2.downloader.listener.IDownloadState;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UpdateService extends Service
        implements IDownloadError, IDownloadProgress, IDownloadState {

    private Notification notification;
    private NotificationManager manager;
    private static final int NOTIFICATION_ID = 1;
    public static final String KEY_URL = "url";
    public static final String KEY_FILE_PATH = "file_path";
    private boolean isAlive = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isAlive) return super.onStartCommand(intent, flags, startId);
        isAlive = true;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.download_begin))
                .setWhen(System.currentTimeMillis())
                .setContent(new RemoteViews(getPackageName(), R.layout.layout_download));
        notification = builder.build();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(NOTIFICATION_ID, notification);

        new SimpleDownloadBuilder()
                .setListener(this, this, this)
                .setName(Config.sAPK_NAME)
                .setNotifyPercent(2)
                .setPath(intent.getStringExtra(KEY_FILE_PATH))
                .setUrl(intent.getStringExtra(KEY_URL))
                .download();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDownloadError(Throwable e) {
        ToastUtils.show(getString(R.string.download_error));
        if (e != null) {
            JLog.e(e.getMessage() + "");
        }
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDownloadProgress(int progress, int total, int percent) {
        notification.contentView.setTextViewText(R.id.tv_progress
                , String.format(getString(R.string.current_progress), percent + "%"));
        notification.contentView.setProgressBar(R.id.pb, 100, percent, false);
        manager.notify(NOTIFICATION_ID, notification);
        JLog.d("进度：" + percent);
    }

    @Override
    public void onDownloadStateChange(State state, File file) {
        switch (state) {
            case IDLE:
//                ToastUtils.show("暂未开始");
                break;
            case RUNNING:
//                ToastUtils.show("正在下载");
                break;
            case STOP:
//                ToastUtils.show("下载停止");
                stopForeground(true);
                stopSelf();
                break;
            case FINISH:
//                ToastUtils.show("下载完成");
                installApk(file.getAbsolutePath());
                stopForeground(true);
                stopSelf();
                break;
        }
    }

    private void installApk(String path) {
        String apkPath = "file://" + path;
        Uri uri = Uri.parse(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
