package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/18.
 */
public class PropertyMessageDetailEvent {

    public long id;

    public PropertyMessageDetailEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
