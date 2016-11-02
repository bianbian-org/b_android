package com.techjumper.lib2.downloader.listener;


import java.io.File;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IDownloadState {
    enum State {
        IDLE,
        RUNNING,
        STOP,
        FINISH
    }

    void onDownloadStateChange(State state, File downloadFile);
}
