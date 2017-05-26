package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DeletePicNotifyEvent {

    private int position;

    public DeletePicNotifyEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
