package com.techjumper.lib2.downloader.exception;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PathErrorException extends DownloadException {
    public PathErrorException() {
    }

    public PathErrorException(String detailMessage) {
        super(detailMessage);
    }

    public PathErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PathErrorException(Throwable throwable) {
        super(throwable);
    }
}
