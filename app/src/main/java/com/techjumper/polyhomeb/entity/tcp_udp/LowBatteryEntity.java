package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/5/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class LowBatteryEntity extends BaseReceiveEntity<LowBatteryEntity.DataEntity> {

    /**
     * data : {"time":"yyyy-MM-dd HH:mm:ss","sn":"polyhome_157,194"}
     * msg : warning:LowElectricity
     * code : 0
     */


    public static class DataEntity {
        private String time;
        private String sn;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
    }
}
