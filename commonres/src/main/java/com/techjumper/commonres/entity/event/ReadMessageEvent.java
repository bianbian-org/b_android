package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/9.
 */
public class ReadMessageEvent {

    private long id;

    public ReadMessageEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
