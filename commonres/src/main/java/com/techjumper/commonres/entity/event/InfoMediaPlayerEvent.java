package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/8/1.
 */

public class InfoMediaPlayerEvent {

    public static final int PLOY = 0;
    public static final int INFO = 1;

    private int type = PLOY;

    public InfoMediaPlayerEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
