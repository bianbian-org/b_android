package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UploadPicEntity extends BaseEntity<UploadPicEntity.UploadPicDataEntity> {

    public static class UploadPicDataEntity {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "UploadPicDataEntity{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }
}
