package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyRepairEntity extends BaseEntity<PropertyRepairEntity.DataBean> {

    public static class DataBean {
        /**
         * title : 报修
         * content : 这是内容
         * time : 12月23日
         * propertyResponse : 南海是中国的
         * isRelpay : 已回复
         * isRead : 1
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String title;
            private String content;
            private String time;
            private String propertyResponse;
            private boolean isRead;
            private String btnName;

            public String getBtnName() {
                return btnName;
            }

            public void setBtnName(String btnName) {
                this.btnName = btnName;
            }

            public boolean isRead() {
                return isRead;
            }

            public void setRead(boolean read) {
                isRead = read;
            }

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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getPropertyResponse() {
                return propertyResponse;
            }

            public void setPropertyResponse(String propertyResponse) {
                this.propertyResponse = propertyResponse;
            }

        }
    }
}
