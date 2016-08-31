package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/8/29.
 */

public class HeartbeatEntity extends BaseEntity<HeartbeatEntity.HeartbeatDataEntity> {

    public static class HeartbeatDataEntity {
        private String result;
        private long time;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
