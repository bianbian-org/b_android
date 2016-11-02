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
    private IDownloadError iDownloadError;
    private IDownloadState iDownloadState;
    private IDownloadProgress iDownloadProgress;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
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
        return new SimpleDownloader(url, path, name, iDownloadProgress, iDownloadState, iDownloadError);
    }

    public SimpleDownloader download() {
        SimpleDownloader simpleDownloader = new SimpleDownloader(url, path, name, iDownloadProgress, iDownloadState, iDownloadError);
        simpleDownloader.download();
        return simpleDownloader;
    }
}
