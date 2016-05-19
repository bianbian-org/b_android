package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/18.
 */
public class ComplaintDetailEntity extends BaseEntity<ComplaintDetailEntity.ComplaintDetailDataEntity>{

    public static class ComplaintDetailDataEntity {
        private long id;
        private String content;
        private String user_name;
        private int status;
        private int types;
        private long user_id;

        List<ReplyEntity> replies;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTypes() {
            return types;
        }

        public void setTypes(int types) {
            this.types = types;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public List<ReplyEntity> getReplies() {
            return replies;
        }

        public void setReplies(List<ReplyEntity> replies) {
            this.replies = replies;
        }
    }
}
