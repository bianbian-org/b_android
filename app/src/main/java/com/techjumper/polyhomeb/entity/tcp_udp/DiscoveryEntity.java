package com.techjumper.polyhomeb.entity.tcp_udp;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/4/6
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DiscoveryEntity extends BaseReceiveEntity<DiscoveryEntity.DataEntity> {

    private String sn;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public static class DataEntity {
        /**
         * value : 19ug/m^3
         * type : PM2.5
         */

        private List<ListEntity> list;

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public static class ListEntity {
            private String value;
            private String type;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
