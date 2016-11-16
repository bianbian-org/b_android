package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/8/29.
 */

public class HeartbeatEntity extends BaseEntity<HeartbeatEntity.HeartbeatDataEntity> {

    public static class HeartbeatDataEntity {
        private String result;
        private long time;
        private String ticket;
        private int update_ad; //更新广告 0-不更新 1-更新

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

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public int getUpdate_ad() {
            return update_ad;
        }

        public void setUpdate_ad(int update_ad) {
            this.update_ad = update_ad;
        }
    }
}
