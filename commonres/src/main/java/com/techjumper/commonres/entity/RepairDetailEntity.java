package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/20.
 */
public class RepairDetailEntity extends BaseEntity<RepairDetailEntity.RepairDetailDataEntity> {

    public static class RepairDetailDataEntity {

        private long id;
        private int repair_type;
        private int repair_device;
        private int status;
        private String repair_date;
        private String note;

        List<ReplyEntity> replies;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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

        public List<ReplyEntity> getReplies() {
            return replies;
        }

        public void setReplies(List<ReplyEntity> replies) {
            this.replies = replies;
        }
    }
}
