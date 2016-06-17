package com.techjumper.commonres.entity.event.loadmoreevent;

/**
 * Created by kevin on 16/6/17.
 */
public class LoadmorePresenterEvent {
    private int type;

    public LoadmorePresenterEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
