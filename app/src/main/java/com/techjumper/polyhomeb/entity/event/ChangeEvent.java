package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChangeEvent {

    private int type;  //type为1代表更新昵称,type为2代表更新头像

    public ChangeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
