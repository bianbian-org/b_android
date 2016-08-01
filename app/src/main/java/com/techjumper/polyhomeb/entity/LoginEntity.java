package com.techjumper.polyhomeb.entity;

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
                    '}';
        }
    }
}
