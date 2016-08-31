package com.techjumper.commonres.entity.event;

/**
 * 通知更新时间ui
 * Created by kevin on 16/7/1.
 */
public class TimeEvent {

    public static String JUJIA = "jujia";
    public static String SHOPPING = "shopping";
    public static String MAIN = "main";

    private String type;
    private long time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
