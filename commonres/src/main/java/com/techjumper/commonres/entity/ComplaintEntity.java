package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/17.
 */
public class ComplaintEntity extends BaseEntity<ComplaintEntity.ComplaintListEntity> {

    public static class ComplaintListEntity {
        private List<ComplaintDataEntity> suggestions;

        public List<ComplaintDataEntity> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<ComplaintDataEntity> suggestions) {
            this.suggestions = suggestions;
        }
    }

    public static class ComplaintDataEntity {
        private long id;
        private long user_id;
        private String content;
        private String user_name;
        private int status;
        private int types;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
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
    }
}
