package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/18.
 */
public class PropertyMessageDetailEvent {

    public static final int REPAIR = 0;
    public static final int COMPLAINT = 1;

    private long id;
    private int type;

    public PropertyMessageDetailEvent(long id, int type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
