package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageEntity extends BaseEntity<MessageEntity.DataBean> {

    public static class DataBean {
        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private int all_count;   //总数
            /**
             * id : 2
             * title : 2016-04-2614: 03: 52
             * types : 1
             * content : 门磁
             * obj_id : 12
             * has_read : 0
             * created_at : 1467254681 创建时间
             */

            private List<MessagesBean> messages;

            public int getAll_count() {
                return all_count;
            }

            public void setAll_count(int all_count) {
                this.all_count = all_count;
            }

            public List<MessagesBean> getMessages() {
                return messages;
            }

            public void setMessages(List<MessagesBean> messages) {
                this.messages = messages;
            }

            public static class MessagesBean {
                private int id;
                private String title;
                private String types;
                private String content;
                private String obj_id;
                private String has_read;
                private String created_at;

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

                public String getTypes() {
                    return types;
                }

                public void setTypes(String types) {
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

                public String getHas_read() {
                    return has_read;
                }

                public void setHas_read(String has_read) {
                    this.has_read = has_read;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }
            }
        }
    }
}
