package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class BluetoothLockDoorInfoEntity extends BaseEntity<BluetoothLockDoorInfoEntity.DataBean> {

    public static class DataBean {
        private int has_bluelock;
        /**
         * name : 小区0015号围墙机
         * sn : 0393554485
         * mac : 88: 55: 17: 75: 2a: 35
         * ekey : ecb3158c7ebbdd27672d2e6896921c77000000000000000000000000000000001000
         */

        private List<InfosBean> infos;

        public int getHas_bluelock() {
            return has_bluelock;
        }

        public void setHas_bluelock(int has_bluelock) {
            this.has_bluelock = has_bluelock;
        }

        public List<InfosBean> getInfos() {
            return infos;
        }

        public void setInfos(List<InfosBean> infos) {
            this.infos = infos;
        }

        public static class InfosBean {
            private String name;
            private String sn;
            private String mac;
            private String ekey;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getEkey() {
                return ekey;
            }

            public void setEkey(String ekey) {
                this.ekey = ekey;
            }

            @Override
            public String toString() {
                return "InfosBean{" +
                        "name='" + name + '\'' +
                        ", sn='" + sn + '\'' +
                        ", mac='" + mac + '\'' +
                        ", ekey='" + ekey + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "has_bluelock=" + has_bluelock +
                    ", infos=" + infos +
                    '}';
        }
    }
}
