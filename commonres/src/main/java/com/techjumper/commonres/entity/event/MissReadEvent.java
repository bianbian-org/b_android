package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/7/27.
 */

public class MissReadEvent {

    private int num;

    public MissReadEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
