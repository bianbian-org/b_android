package com.techjumper.lib2.downloader.exception;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DownloadException extends RuntimeException {
    public DownloadException() {
    }

    public DownloadException(String detailMessage) {
        super(detailMessage);
    }

    public DownloadException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DownloadException(Throwable throwable) {
        super(throwable);
    }
}
