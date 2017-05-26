package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JSArticleIdEvent {

    private String id;

    public JSArticleIdEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
