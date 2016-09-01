package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/8/29.
 */

public class HeartbeatTimeEntity extends BaseEntity<HeartbeatTimeEntity.HeartbeatTimeDataEntity> {

    public static class HeartbeatTimeDataEntity {
        private String name;
        private HeartbeatTimeItemEntity values;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HeartbeatTimeItemEntity getValues() {
            return values;
        }

        public void setValues(HeartbeatTimeItemEntity values) {
            this.values = values;
        }
    }

    public static class HeartbeatTimeItemEntity {
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
