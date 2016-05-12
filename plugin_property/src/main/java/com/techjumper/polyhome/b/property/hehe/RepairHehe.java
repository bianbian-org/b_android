package com.techjumper.polyhome.b.property.hehe;

import java.util.List;

/**
 * Created by kevin on 16/5/12.
 */
public class RepairHehe {

    public static final int HASREAD_FALSE = 0;
    public static final int HASREAD_TURE = 1;

    public static final int TYPE_RESPONSE = 1;
    public static final int TYPE_SUBMIT = 2;
    public static final int TYPE_FINISH = 3;

    public String title;
    public String content;
    public String date;
    public int type;
    public int hasRead;
    public List<String> propertyInfo;
    public List<String> userInfo;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getPropertyInfo() {
        return propertyInfo;
    }

    public void setPropertyInfo(List<String> propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    public List<String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<String> userInfo) {
        this.userInfo = userInfo;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }
}
