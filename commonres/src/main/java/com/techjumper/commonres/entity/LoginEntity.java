package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/5/9.
 */
public class LoginEntity extends BaseEntity<LoginEntity.LoginDataEntity>{

    public static final int MALE = 1;
    public static final int FRMALE = 2;

    public static class LoginDataEntity{
        private int id;
        private int sex;
        private String email;
        private String cover;
        private String ticket;
        private String birthday;
        private String username;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
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
