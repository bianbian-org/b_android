package com.techjumper.polyhomeb.entity.tcp_udp;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class InfraredKeyEntity extends BaseReceiveEntity<InfraredKeyEntity.DataEntity> {


    public static class DataEntity {
        private String sn;
        /**
         * key : 3
         * name : vc
         */

        private List<ListEntity> list;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public static class ListEntity {
            private String key;
            private String name;

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
}
