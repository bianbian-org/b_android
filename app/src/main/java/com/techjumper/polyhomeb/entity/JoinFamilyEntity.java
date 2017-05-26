package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JoinFamilyEntity extends BaseEntity<JoinFamilyEntity.DataBean> {

    /**
     * family_name : 家庭001
     * village_id : 1
     * family_id : 1
     */

    public static class DataBean {
        private String family_name;
        private int village_id;
        private int family_id;

        public String getFamily_name() {
            return family_name;
        }

        public void setFamily_name(String family_name) {
            this.family_name = family_name;
        }

        public int getVillage_id() {
            return village_id;
        }

        public void setVillage_id(int village_id) {
            this.village_id = village_id;
        }

        public int getFamily_id() {
            return family_id;
        }

        public void setFamily_id(int family_id) {
            this.family_id = family_id;
        }
    }
}
