package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/17.
 */
public class RepairEntity extends BaseEntity<RepairEntity.RepairListEntity>{

    public static final int STATUS_NOTHING = 0;
    public static final int STATUS_RESPONSE = 1;
    public static final int STATUS_SUBMIT = 2;
    public static final int STATUS_FINISH = 3;

    public static class RepairListEntity {
        List<RepairDataEntity> repairs;

        public List<RepairDataEntity> getRepairs() {
            return repairs;
        }

        public void setRepairs(List<RepairDataEntity> repairs) {
            this.repairs = repairs;
        }
    }

    public static class RepairDataEntity {
        long id;
        int repair_type;
        int repair_device;
        int status;
        String repair_date;
        String note;

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
    }
}
