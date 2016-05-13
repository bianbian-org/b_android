package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/13.
 */
public class PropertyActionEvent {

    private int type;
    private boolean isAction;

    public PropertyActionEvent(boolean isAction) {
        this.isAction = isAction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }
}
