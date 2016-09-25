package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MedicalChangeUserInfoEvent {

    private int type;
    private String content;

    private int position;  //点击的item的位置

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public MedicalChangeUserInfoEvent(int type, String content, int position) {
        this.type = type;
        this.content = content;
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
