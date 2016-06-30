package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/4/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SnEntity extends BaseReceiveEntity<SnEntity.DataEntity> {


    public static class DataEntity {
        private String sn;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
    }
}
