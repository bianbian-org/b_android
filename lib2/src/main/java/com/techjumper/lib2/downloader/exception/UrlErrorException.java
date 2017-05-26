package com.techjumper.lib2.downloader.exception;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UrlErrorException extends DownloadException {
    public UrlErrorException() {
    }

    public UrlErrorException(String detailMessage) {
        super(detailMessage);
    }

    public UrlErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UrlErrorException(Throwable throwable) {
        super(throwable);
    }
}
