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

    public void setDownloadErrorListener(IDownloadError iDownloadError) {
        this.iDownloadError = iDownloadError;
    }

    public void setDownloadFinishListener(IDownloadState iDownloadState) {
        this.iDownloadState = iDownloadState;
    }

    public void setDownloadProgressListener(IDownloadProgress iDownloadProgress) {
        this.iDownloadProgress = iDownloadProgress;
    }

    public void setListener(IDownloadProgress iDownloadProgress, IDownloadState iDownloadState, IDownloadError iDownloadError) {
        this.iDownloadError = iDownloadError;
        this.iDownloadState = iDownloadState;
        this.iDownloadProgress = iDownloadProgress;
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
