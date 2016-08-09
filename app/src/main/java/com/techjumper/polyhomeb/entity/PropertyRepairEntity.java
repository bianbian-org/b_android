package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/6
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyRepairEntity extends BaseEntity<PropertyRepairEntity.DataBean> {

    public static class DataBean {
        private int count;
        /**
         * repair_type : 1
         * repair_device : 3
         * status : 0
         * repair_date : 1470301482
         * note : 备注
         * replies : 1212313
         */

        private List<RepairsBean> repairs;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<RepairsBean> getRepairs() {
            return repairs;
        }

        public void setRepairs(List<RepairsBean> repairs) {
            this.repairs = repairs;
        }

        public static class RepairsBean {
            private int repair_type;
            private int repair_device;
            private int status;
            private String repair_date;
            private String note;
            private String replies;
            private int id;
            private int user_id;
            private String user_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
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

            public String getReplies() {
                return replies;
            }

            public void setReplies(String replies) {
                this.replies = replies;
            }
        }
    }
}
