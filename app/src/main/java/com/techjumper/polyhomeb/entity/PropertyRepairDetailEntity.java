package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyRepairDetailEntity extends BaseEntity<PropertyRepairDetailEntity.DataBean> {

    public static class DataBean {
        private int id;
        private int repair_type;
        private int repair_device;
        private int status;
        private String repair_date;
        private String note;
        private String[] imgs;
        private List<RepliesBean> replies;

        public int getRepair_type() {
            return repair_type;
        }

        public void setRepair_type(int repair_type) {
            this.repair_type = repair_type;
        }

        public int getRepair_device() {
            return repair_device;
        }

        public void setRepair_device(int repair_device) {
            this.repair_device = repair_device;
        }

        public String getRepair_date() {
            return repair_date;
        }

        public void setRepair_date(String repair_date) {
            this.repair_date = repair_date;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String[] getImgs() {
            return imgs;
        }

        public void setImgs(String[] imgs) {
            this.imgs = imgs;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<RepliesBean> getReplies() {
            return replies;
        }

        public void setReplies(List<RepliesBean> replies) {
            this.replies = replies;
        }

        public static class RepliesBean {
            private int user_id;
            private String content;
            private String time;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
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

}
