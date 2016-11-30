package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/11/30.
 */

public class PayEvent {
    public static final int RESULT = 0;
    public static final int DETAIL = 1;
    private int type = RESULT;

    public PayEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
