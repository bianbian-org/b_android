package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class InfraredLearnEntity extends BaseReceiveEntity<InfraredLearnEntity.DataEntity> {


    public static class DataEntity {
        private String sn;
        private String key;
        private String name;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
