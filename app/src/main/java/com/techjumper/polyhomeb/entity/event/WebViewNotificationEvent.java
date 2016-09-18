package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WebViewNotificationEvent {

    private String result;

    public WebViewNotificationEvent(String result) {
        this.result = result;
    }

    public String getResult() {

        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
