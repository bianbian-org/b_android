package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class LoginEntity extends BaseEntity<LoginEntity.LoginDataEntity> {

    public static class LoginDataEntity {

        //注册接口的字段
        private String id;
        private String mobile;   //登录接口没有mobile
        private String ticket;
        private String cover;
        private String username;

        //以下字段是登录接口多出来的
        private String sex;
        private String email;
        private String birthday;
        private List<FamiliesBean> families;
        private List<VillagesBean> villages;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public List<FamiliesBean> getFamilies() {
            return families;
        }

        public void setFamilies(List<FamiliesBean> families) {
            this.families = families;
        }

        public List<VillagesBean> getVillages() {
            return villages;
        }

        public void setVillages(List<VillagesBean> villages) {
            this.villages = villages;
        }

        public static class FamiliesBean {
            private String family_id;
            private String family_name;
            private int village_id;

            public int getVillage_id() {
                return village_id;
            }

            public void setVillage_id(int village_id) {
                this.village_id = village_id;
            }

            public String getFamily_id() {
                return family_id;
            }

            public void setFamily_id(String family_id) {
                this.family_id = family_id;
            }

            public String getFamily_name() {
                return family_name;
            }

            public void setFamily_name(String family_name) {
                this.family_name = family_name;
            }
        }

        public static class VillagesBean {
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

        @Override
        public String toString() {
            return "LoginDataEntity{" +
                    "id='" + id + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", ticket='" + ticket + '\'' +
                    ", cover='" + cover + '\'' +
                    ", username='" + username + '\'' +
                    ", sex='" + sex + '\'' +
                    ", email='" + email + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", families=" + families +
                    ", villages=" + villages +
                    '}';
        }
    }
}
