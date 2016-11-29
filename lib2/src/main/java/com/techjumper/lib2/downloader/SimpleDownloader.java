package com.techjumper.lib2.downloader;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.BuildConfig;
import com.techjumper.lib2.downloader.exception.PathErrorException;
import com.techjumper.lib2.downloader.exception.UrlErrorException;
import com.techjumper.lib2.downloader.listener.IDownloadError;
import com.techjumper.lib2.downloader.listener.IDownloadProgress;
import com.techjumper.lib2.downloader.listener.IDownloadState;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SimpleDownloader {

    public static final String DEFAULT_NAME = "unnamed";

    private String mUrl;
    private String mPath;
    private int mNotifyPercent;
    private int mLastPercent;
    private File mSaveFile;
    private IDownloadProgress iDownloadProgress;
    private IDownloadState iDownloadState;
    private IDownloadError iDownloadError;
    private IDownloadState.State mState = IDownloadState.State.IDLE;

    SimpleDownloader(String url, String path, String name, int notifyPercent, IDownloadProgress iDownloadProgress, IDownloadState iDownloadState, IDownloadError iDownloadError) {
        mUrl = url;
        mPath = path;
        mNotifyPercent = notifyPercent;
        String name1 = name;
        if (TextUtils.isEmpty(name1))
            name1 = DEFAULT_NAME;

        mSaveFile = new File(mPath, name1);
        this.iDownloadProgress = iDownloadProgress;
        this.iDownloadState = iDownloadState;
        this.iDownloadError = iDownloadError;
    }

    public void download() {
        if (mState == IDownloadState.State.RUNNING)
            return;
        Observable.just(mUrl)
                .map(url -> {
                    if (!init())
                        return null;
                    if (mSaveFile.exists()) {
                        if (!mSaveFile.delete()) {
                            notifyError(new PathErrorException("文件已存在，并且无法删除"));
                            return null;
                        }
                    }
                    changeStateAndNotify(IDownloadState.State.RUNNING);
                    URL tURL;
                    try {
                        tURL = new URL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        notifyError(e);
                        return null;
                    }

                    HttpURLConnection conn;
                    BufferedInputStream bis = null;
                    BufferedOutputStream bos = null;
                    try {
                        conn = (HttpURLConnection) tURL.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setReadTimeout(20000);
                        conn.setConnectTimeout(15000);
                        int responseCode = conn.getResponseCode();
                        if (!(responseCode == 200)) {
                            notifyError(new NetworkErrorException("server error, code=" + responseCode));
                            return null;
                        }
                        int length = conn.getContentLength();
                        bis = new BufferedInputStream(conn.getInputStream());
                        bos = new BufferedOutputStream(new FileOutputStream(mSaveFile));
                        byte[] buffer = new byte[8000];
                        int progress = 0;
                        for (int count; (count = bis.read(buffer)) != -1; ) {
                            bos.write(buffer, 0, count);
                            progress += count;
                            notifyProgress(progress, length);
                        }
                        changeStateAndNotify(IDownloadState.State.FINISH);
                    } catch (IOException e) {
                        e.printStackTrace();
                        notifyError(e);
                        return null;
                    } finally {
                        try {
                            bis.close();
                            bos.close();
                        } catch (Exception ignored) {
                        }
                    }


                    return mSaveFile;
                })
                .compose(CommonWrap.wrap())
                .subscribe(new Subscriber<File>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   changeStateAndNotify(IDownloadState.State.STOP);
                               }

                               @Override
                               public void onNext(File file) {

                               }
                           }

                );

    }

    public File getFile() {
        return mSaveFile;
    }

    public IDownloadState.State getState() {
        return mState;
    }

    private boolean init() {

        if (TextUtils.isEmpty(mUrl)) {
            notifyError(new UrlErrorException("下载地址为空"));
            return false;
        }

        if (TextUtils.isEmpty(mPath)) {
            notifyError(new PathErrorException("路径不能为空"));
            return false;
        }
        File saveDirFile = new File(mPath);
        if (!saveDirFile.exists()) {
            if (!saveDirFile.mkdirs()) {
                notifyError(new PathErrorException("路径创建失败"));
                return false;
            }
        } else if (saveDirFile.isFile()) {
            if (!saveDirFile.delete()) {
                notifyError(new PathErrorException("路径创建失败"));
                return false;
            }
            if (!saveDirFile.exists() && !saveDirFile.mkdirs()) {
                notifyError(new PathErrorException("路径创建失败"));
                return false;
            }
        }

        return true;
    }

    private void notifyError(Throwable e) {
        changeStateAndNotify(IDownloadState.State.STOP);
        mLastPercent = 0;
        Utils.mainHandler.post(() -> {
            if (BuildConfig.DEBUG) {
                ToastUtils.show("网络错误，下载失败");
            }
            if (iDownloadError != null)
                iDownloadError.onDownloadError(e);
        });
    }

    private void notifyProgress(int progress, int total) {
        int percent = progress * 100 / total;
        if (mNotifyPercent != 0) {
            if (percent - mLastPercent < mNotifyPercent)
                return;
        }
        mLastPercent = percent;
        Utils.mainHandler.post(() -> {
            if (iDownloadProgress != null)
                iDownloadProgress.onDownloadProgress(progress, total, percent);
        });
    }

    private void changeStateAndNotify(IDownloadState.State state) {
        mState = state;
        if (state == IDownloadState.State.FINISH)
            mLastPercent = 0;
        Utils.mainHandler.post(() -> {
            if (iDownloadState != null)
                iDownloadState.onDownloadStateChange(state, mSaveFile);
        });
    }

}
