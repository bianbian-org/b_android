package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/5.
 */
public class InfoDetailEvent {

    private String title;
    private String content;
    private int type; //消息类型 1-系统信息 2-订单信息 3-医疗信息
    private String created_at;

    public InfoDetailEvent(String title, String content, int type, String created_at) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.created_at = created_at;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
