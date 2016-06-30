package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/5/16
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WayStatusSnEntity extends BaseReceiveEntity<WayStatusSnEntity.DataEntity> {

    public static class DataEntity {
        private String way;
        private String status;
        private String sn;

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
    }
}
