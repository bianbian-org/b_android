package com.techjumper.polyhomeb.entity.event;

import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ResendMessageEvent {

    private String content;
    private int position;
    private PropertyRepairDetailProprietorContentData data;

    public PropertyRepairDetailProprietorContentData getData() {
        return data;
    }

    public void setData(PropertyRepairDetailProprietorContentData data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ResendMessageEvent(String content, int position, PropertyRepairDetailProprietorContentData data) {
        this.content = content;
        this.position = position;
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
