package com.techjumper.polyhomeb.entity.emqtt;

/**
 * Created by Admin on 2017/6/20.
 */

public class PropertyEmqttUpdateEvent {
    /**
     * 推送消息过来时，所处界面正好物业本界面，当用户点击推送的消息时，可以把数据更新过来
     */
    private int position;

    public PropertyEmqttUpdateEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
