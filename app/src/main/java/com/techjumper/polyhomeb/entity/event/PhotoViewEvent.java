package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PhotoViewEvent {

    private String mCurrentUrl;

    public PhotoViewEvent(String mCurrentUrl) {
        this.mCurrentUrl = mCurrentUrl;
    }

    public String getCurrentUrl() {
        return mCurrentUrl;
    }

    public void setCurrentUrl(String mCurrentUrl) {
        this.mCurrentUrl = mCurrentUrl;
    }
}
