package com.techjumper.polyhomeb.adapter.recycler_Data;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageAllContentData {

    private String title;
    private String content;
    private String rightText;
    private String time;
    private int has_read;
    private int id;
    private String obj_id;
    private String types;

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public int getHas_read() {
        return has_read;
    }

    public void setHas_read(int has_read) {
        this.has_read = has_read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
