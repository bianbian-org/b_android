package com.techjumper.commonres.entity.event;

/**
 * 对于返回键的一些操作
 * Created by kevin on 16/5/16.
 */
public class BackEvent {

    public static final int PROPERTY_ACTION = 0;
    public static final int PROPERTY_LIST = 1;
    public static final int FINISH = 2;
    public static final int INFO_LIST = 3;

    private int type;

    public BackEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
