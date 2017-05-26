package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RefreshComplainListDataEvent {
    private int complainStatus;

    public int getComplainStatus() {
        return complainStatus;
    }

    public void setComplainStatus(int complainStatus) {
        this.complainStatus = complainStatus;
    }

    public RefreshComplainListDataEvent(int complainStatus) {
        this.complainStatus = complainStatus;
    }
}
