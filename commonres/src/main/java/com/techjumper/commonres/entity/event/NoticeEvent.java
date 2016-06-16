package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/6/16.
 */
public class NoticeEvent {

    private String title;
    private String content;
    private int type;

    public NoticeEvent(String title, String content, int type) {
        this.title = title;
        this.content = content;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
