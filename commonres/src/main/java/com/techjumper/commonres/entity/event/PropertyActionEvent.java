package com.techjumper.commonres.entity.event;

/**
 * 决定显示填写和
 * Created by kevin on 16/5/13.
 */
public class PropertyActionEvent {

    private int type;
    private boolean isAction;
    private int listType;

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

    public int getListType() {
        return listType;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }
}
