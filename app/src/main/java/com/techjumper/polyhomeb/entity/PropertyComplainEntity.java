package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyComplainEntity extends BaseEntity<PropertyComplainEntity.DataBean> {

    public static class DataBean {
        /**
         * id : 8
         * content : toutoutoutotuotutu
         * user_name : 啦啦啦
         * status : 0
         * types : 1
         * user_id : 1
         * created_at : 时间
         * replies : 1212313
         */

        private List<SuggestionsBean> suggestions;
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<SuggestionsBean> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<SuggestionsBean> suggestions) {
            this.suggestions = suggestions;
        }

        public static class SuggestionsBean {
            private int id;
            private String content;
            private String user_name;
            private int status;
            private int types;
            private int user_id;
            private String created_at;
            private String replies;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getReplies() {
                return replies;
            }

            public void setReplies(String replies) {
                this.replies = replies;
            }

            @Override
            public String toString() {
                return "SuggestionsBean{" +
                        "id=" + id +
                        ", content='" + content + '\'' +
                        ", user_name='" + user_name + '\'' +
                        ", status=" + status +
                        ", types=" + types +
                        ", user_id=" + user_id +
                        ", created_at='" + created_at + '\'' +
                        ", replies='" + replies + '\'' +
                        '}';
            }
        }
    }
}
