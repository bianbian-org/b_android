package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/9.
 */
public class ReadMessageEvent {

    private long id;
    private int type;

    public ReadMessageEvent(long id, int type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
