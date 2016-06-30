package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class FreshAirQueryEntity extends BaseReceiveEntity<FreshAirQueryEntity.DataEntity> {


    public static class DataEntity {
        private String wind;
        private String status;
        private String sn;
        private String mode;

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
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

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
