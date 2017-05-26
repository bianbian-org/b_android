package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyPlacardDetailEntity extends BaseEntity<PropertyPlacardDetailEntity.DataBean> {
    /**
     * id : 10
     * title : 54trerf
     * types : 1
     * content : u003cpu003e3243erewu003c/pu003e
     * time : 2016-05-06
     */
    public static class DataBean {
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
    }
}
