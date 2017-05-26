package com.techjumper.lib2.downloader.listener;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IDownloadError {
    void onDownloadError(Throwable e);
}
