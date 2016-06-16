package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/6/16.
 */
public class NoticeEntity extends BaseEntity<NoticeEntity.NoticeDataEntity> {
    public static final int PROPERTY = 0; //物业公共
    public static final int SYSTEM = 1; //系统信息
    public static final int ORDER = 2; //订单信息
    public static final int MEDICAL = 3; //医疗信息

    public static class NoticeDataEntity {
        List<Message> messages;
        List<Unread> unread;

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

        public List<Unread> getUnread() {
            return unread;
        }

        public void setUnread(List<Unread> unread) {
            this.unread = unread;
        }

        @Override
        public String toString() {
            return "NoticeDataEntity{" +
                    "messages=" + messages +
                    ", unread=" + unread +
                    '}';
        }
    }

    public class Message {
        String title;
        String content;
        String updated_at;
        int types;

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

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getTypes() {
            return types;
        }

        public void setTypes(int types) {
            this.types = types;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", types=" + types +
                    '}';
        }
    }

    public class Unread {
        int type;
        int count;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "Unread{" +
                    "type=" + type +
                    ", count=" + count +
                    '}';
        }
    }
}
