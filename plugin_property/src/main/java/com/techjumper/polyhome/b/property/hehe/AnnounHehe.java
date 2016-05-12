package com.techjumper.polyhome.b.property.hehe;

/**
 * Created by kevin on 16/5/12.
 */
public class AnnounHehe {

    public static final int HASREAD_FALSE = 0;
    public static final int HASREAD_TURE = 1;

    public String title;
    public String content;
    public int hasRead;
    public String date;

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

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
