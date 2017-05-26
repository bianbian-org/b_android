package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AvatarEntity extends BaseEntity<AvatarEntity.DataBean> {

    public static class DataBean {
        private String cover;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
