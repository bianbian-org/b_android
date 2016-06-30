package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/5/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SceneIdEntity extends BaseReceiveEntity<SceneIdEntity.DataEntity> {
    public static class DataEntity {
        private String senceid;

        public String getSenceid() {
            return senceid;
        }

        public void setSenceid(String senceid) {
            this.senceid = senceid;
        }
    }
}
