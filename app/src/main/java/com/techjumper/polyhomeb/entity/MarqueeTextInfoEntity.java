package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MarqueeTextInfoEntity extends BaseEntity<MarqueeTextInfoEntity.DataBean> {

    public static class DataBean {
        private List<MessagesBean> messages;

        public List<MessagesBean> getMessages() {
            return messages;
        }

        public void setMessages(List<MessagesBean> messages) {
            this.messages = messages;
        }

        public static class MessagesBean {
            /**
             * id : 10
             * title : 您的建议有新的回复
             * types : 5
             * content : 尊敬的用户，您的建议已有物业的新回复，请注意查收
             * obj_id : 8
             * has_read : 0
             * created_at : 1467254939
             */

            private int id;
            private String title;
            private int types;
            private String content;
            private String obj_id;
            private int has_read;
            private int created_at;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getTypes() {
                return types;
            }

            public void setTypes(int types) {
                this.types = types;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getObj_id() {
                return obj_id;
            }

            public void setObj_id(String obj_id) {
                this.obj_id = obj_id;
            }

            public int getHas_read() {
                return has_read;
            }

            public void setHas_read(int has_read) {
                this.has_read = has_read;
            }

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }
        }
    }
}
