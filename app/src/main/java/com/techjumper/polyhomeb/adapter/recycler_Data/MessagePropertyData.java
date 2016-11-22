package com.techjumper.polyhomeb.adapter.recycler_Data;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MessagePropertyData {

    private String title;
    private String content;
    private String rightText;
    private String time;
    private String type;
    private String obj_id;
    private int has_read;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHas_read() {
        return has_read;
    }

    public void setHas_read(int has_read) {
        this.has_read = has_read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
