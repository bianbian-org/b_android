package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyPlacardEntity extends BaseEntity<PropertyPlacardEntity.DataBean> {

    public static class DataBean {

        private List<NoticesBean> notices;

        public List<NoticesBean> getNotices() {
            return notices;
        }

        public void setNotices(List<NoticesBean> notices) {
            this.notices = notices;
        }

        public static class NoticesBean {
            private int id;
            private String title;
            private int types;
            private String content;
            private String time;

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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            @Override
            public String toString() {
                return "NoticesBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", types=" + types +
                        ", content='" + content + '\'' +
                        ", time='" + time + '\'' +
                        '}';
            }
        }
    }
}
