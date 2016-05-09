package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/9.
 */
public class InfoTypeEvent {

    private int type;

    public InfoTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
