package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/4.
 */
public class InfoEntity extends BaseEntity<InfoEntity.InfoDataEntity> {

    public static final int TYPE_SYSTEM = 1;
    public static final int TYPE_ORDER = 2;
    public static final int TYPE_MEDICAL = 3;

    public static final int HASREAD_FALSE = 0;
    public static final int HASREAD_TURE = 2;

    public static class InfoDataEntity {
        List<InfoItemEntity> messages;

        public List<InfoItemEntity> getMessages() {
            return messages;
        }

        public void setMessages(List<InfoItemEntity> messages) {
            this.messages = messages;
        }

        public static class InfoItemEntity {
            private int id;
            private String title;
            private int types; // #消息类型 1-系统信息 2-订单信息 3-医疗信息
            private String content;
            private int obj_id; //对象id，如订单id
            private int has_read; //是否阅读，0-未阅读 2-已阅读
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

            public int getObj_id() {
                return obj_id;
            }

            public void setObj_id(int obj_id) {
                this.obj_id = obj_id;
            }

            public int getHas_read() {
                return has_read;
            }

            public void setHas_read(int has_read) {
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
