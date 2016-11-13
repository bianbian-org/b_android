package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class C_AllMemberEntity extends BaseEntity<C_AllMemberEntity.DataEntity> {

    public static class DataEntity {

        private List<UsersEntity> users;

        public void setUsers(List<UsersEntity> users) {
            this.users = users;
        }

        public List<UsersEntity> getUsers() {
            return users;
        }

        public static class UsersEntity {
            private int id;
            private String username;

            @Override
            public String toString() {
                return "UsersEntity{" +
                        "id=" + id +
                        ", username=" + username +
                        '}';
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getId() {
                return id;
            }

            public String getUsername() {
                return username;
            }
        }
    }
}
