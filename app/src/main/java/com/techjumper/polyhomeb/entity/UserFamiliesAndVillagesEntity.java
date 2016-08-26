package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UserFamiliesAndVillagesEntity extends BaseEntity<UserFamiliesAndVillagesEntity.DataBean> {

    public static class DataBean {
        /**
         * id : 1
         * family_name : 家庭001
         */

        private List<FamilyInfosBean> family_infos;
        /**
         * village_id : 2
         * verified : 0   #审核是否通过 0-未通过 1-已通过
         * village_name : 小区002
         */

        private List<VillageInfosBean> village_infos;

        public List<FamilyInfosBean> getFamily_infos() {
            return family_infos;
        }

        public void setFamily_infos(List<FamilyInfosBean> family_infos) {
            this.family_infos = family_infos;
        }

        public List<VillageInfosBean> getVillage_infos() {
            return village_infos;
        }

        public void setVillage_infos(List<VillageInfosBean> village_infos) {
            this.village_infos = village_infos;
        }

        public static class FamilyInfosBean {
            private int id;
            private String family_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFamily_name() {
                return family_name;
            }

            public void setFamily_name(String family_name) {
                this.family_name = family_name;
            }
        }

        public static class VillageInfosBean {
            private int village_id;
            private int verified;
            private String village_name;

            public int getVillage_id() {
                return village_id;
            }

            public void setVillage_id(int village_id) {
                this.village_id = village_id;
            }

            public int getVerified() {
                return verified;
            }

            public void setVerified(int verified) {
                this.verified = verified;
            }

            public String getVillage_name() {
                return village_name;
            }

            public void setVillage_name(String village_name) {
                this.village_name = village_name;
            }
        }
    }
}
