package com.techjumper.polyhomeb.entity.event;

import com.techjumper.polyhomeb.entity.BaseEntity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class VillageEntity extends BaseEntity<VillageEntity.DataBean> {

    public static class DataBean {
        /**
         * province : 北京市
         * villages : [{"id":1,"name":"小区001"},{"id":2,"name":"小区002"}]
         */

        private List<InfosBean> infos;

        public List<InfosBean> getInfos() {
            return infos;
        }

        public void setInfos(List<InfosBean> infos) {
            this.infos = infos;
        }

        public static class InfosBean {
            private String province;
            /**
             * id : 1
             * name : 小区001
             */

            private List<VillagesBean> villages;

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public List<VillagesBean> getVillages() {
                return villages;
            }

            public void setVillages(List<VillagesBean> villages) {
                this.villages = villages;
            }

            public static class VillagesBean {
                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
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
}
