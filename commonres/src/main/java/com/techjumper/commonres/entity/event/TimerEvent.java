package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/8/8.
 */

public class TimerEvent {

    private boolean isTimer;

    public TimerEvent(boolean isTimer) {
        this.isTimer = isTimer;
    }

    public boolean isTimer() {
        return isTimer;
    }

    public void setTimer(boolean timer) {
        isTimer = timer;
    }
}
