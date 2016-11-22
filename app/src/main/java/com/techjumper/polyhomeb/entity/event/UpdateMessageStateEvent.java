package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class UpdateMessageStateEvent {

    private int id;

    public UpdateMessageStateEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
