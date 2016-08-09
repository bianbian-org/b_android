package com.techjumper.polyhome_b.adlib.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdEntity {
    private HashMap<String, HashMap<String, RulesEntity>> rules;
    private List<HashMap<String, List<AdsEntity>>> ads;


    public HashMap<String, HashMap<String, RulesEntity>> getRules() {
        return rules;
    }

    public void setRules(HashMap<String, HashMap<String, RulesEntity>> rules) {
        this.rules = rules;
    }

    public List<HashMap<String, List<AdsEntity>>> getAds() {
        return ads;
    }

    public void setAds(List<HashMap<String, List<AdsEntity>>> ads) {
        this.ads = ads;
    }

    @Override
    public String toString() {
        return "AdEntity{" +
                "rules=" + rules +
                ", ads=" + ads +
                '}';
    }

    public static class RulesEntity {
        private String id;
        private String time_length;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime_length() {
            return time_length;
        }

        public void setTime_length(String time_length) {
            this.time_length = time_length;
        }

        @Override
        public String toString() {
            return "RulesEntity{" +
                    "id='" + id + '\'' +
                    ", time_length='" + time_length + '\'' +
                    '}';
        }
    }

    public static class AdsEntity implements Serializable {
        private String media_type;
        private String running_time;
        private String md5;
        private String url;
        private String media_url;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public String getRunning_time() {
            return running_time;
        }

        public void setRunning_time(String running_time) {
            this.running_time = running_time;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMedia_url() {
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        @Override
        public String toString() {
            return "AdsEntity{" +
                    "media_type='" + media_type + '\'' +
                    ", running_time='" + running_time + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", url='" + url + '\'' +
                    ", media_url='" + media_url + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

}
