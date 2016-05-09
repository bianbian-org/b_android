package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/5/9.
 */
public class LoginEntity extends BaseEntity<LoginEntity.LoginDataEntity>{

    public static class LoginDataEntity{
        private int id;
        private String mobile;
        private String username;
        private String cover;
        private String ticket;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }
    }
}
