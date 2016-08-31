package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/8/30.
 */

public class HeartbeatTimeEvent {

    private long time;

    public HeartbeatTimeEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
