package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/4/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class EditSceneResponseEntity extends BaseReceiveEntity<EditSceneResponseEntity.DataBean> {

    public static class DataBean {
        private String state;
        private String sencename;
        private String senceid;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getSencename() {
            return sencename;
        }

        public void setSencename(String sencename) {
            this.sencename = sencename;
        }

        public String getSenceid() {
            return senceid;
        }

        public void setSenceid(String senceid) {
            this.senceid = senceid;
        }
    }
}
