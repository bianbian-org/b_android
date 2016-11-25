package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class ADEntity extends BaseEntity<ADEntity.DataBean> {

    public static class DataBean {
        private List<AdInfosBean> ad_infos;

        public List<AdInfosBean> getAd_infos() {
            return ad_infos;
        }

        public void setAd_infos(List<AdInfosBean> ad_infos) {
            this.ad_infos = ad_infos;
        }

        public static class AdInfosBean {
            /**
             * media_type : 1  #媒体类型 1-图片 2-视频
             * media_url : /upload/files/206.jpg
             * url : http: //www.baidu.com
             */
            private int media_type;
            private String media_url;
            private String url;
            private String running_time;

            public String getRunning_time() {
                return running_time;
            }

            public void setRunning_time(String running_time) {
                this.running_time = running_time;
            }

            public int getMedia_type() {
                return media_type;
            }

            public void setMedia_type(int media_type) {
                this.media_type = media_type;
            }

            public String getMedia_url() {
                return media_url;
            }

            public void setMedia_url(String media_url) {
                this.media_url = media_url;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
