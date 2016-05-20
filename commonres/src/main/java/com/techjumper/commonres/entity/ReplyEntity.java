package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/5/19.
 */
public class ReplyEntity {

    private long user_id;
    private String content;
    private String time;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
