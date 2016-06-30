package com.techjumper.polyhomeb.entity.tcp_udp;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/3/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AddDeviceListEntity extends BaseReceiveEntity<AddDeviceListEntity.DataBean> {

    public static class DataBean {
        /**
         * type : 8
         * sn : polyhome_157,198
         * name : 智能门锁
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String type;
            private String sn;
            private String name;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
