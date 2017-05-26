package com.techjumper.lib2.downloader;

import com.techjumper.lib2.downloader.listener.IDownloadError;
import com.techjumper.lib2.downloader.listener.IDownloadState;
import com.techjumper.lib2.downloader.listener.IDownloadProgress;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SimpleDownloadBuilder {
    private String url;
    private String path;
    private String name;
    private int notifyPercent = 2;
    private IDownloadError iDownloadError;
    private IDownloadState iDownloadState;
    private IDownloadProgress iDownloadProgress;

    public SimpleDownloadBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public SimpleDownloadBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public SimpleDownloadBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 设置每隔多少个百分比通知一次。如果<=0则一直通知，但会有性能问题。
     */
    public SimpleDownloadBuilder setNotifyPercent(int percent) {
        if (percent < 0)
            percent = 0;
        else if (percent > 100)
            percent = 100;
        this.notifyPercent = percent;
        return this;
    }

    public SimpleDownloadBuilder setDownloadErrorListener(IDownloadError iDownloadError) {
        this.iDownloadError = iDownloadError;
        return this;
    }

    public SimpleDownloadBuilder setDownloadFinishListener(IDownloadState iDownloadState) {
        this.iDownloadState = iDownloadState;
        return this;
    }

    public SimpleDownloadBuilder setDownloadProgressListener(IDownloadProgress iDownloadProgress) {
        this.iDownloadProgress = iDownloadProgress;
        return this;
    }

    public SimpleDownloadBuilder setListener(IDownloadProgress iDownloadProgress, IDownloadState iDownloadState, IDownloadError iDownloadError) {
        this.iDownloadError = iDownloadError;
        this.iDownloadState = iDownloadState;
        this.iDownloadProgress = iDownloadProgress;
        return this;
    }

    public SimpleDownloader build() {
        return new SimpleDownloader(url, path, name, notifyPercent, iDownloadProgress, iDownloadState, iDownloadError);
    }

    public SimpleDownloader download() {
        SimpleDownloader simpleDownloader = new SimpleDownloader(url, path, name, notifyPercent, iDownloadProgress, iDownloadState, iDownloadError);
        simpleDownloader.download();
        return simpleDownloader;
    }
}
