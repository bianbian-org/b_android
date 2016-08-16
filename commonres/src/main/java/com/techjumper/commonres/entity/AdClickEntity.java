package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/17.
 */
public class AdClickEntity {

    private List<AdClickItemEntity> clicks;

    public List<AdClickItemEntity> getClicks() {
        return clicks;
    }

    public void setClicks(List<AdClickItemEntity> clicks) {
        this.clicks = clicks;
    }

    public static class AdClickItemEntity {
        private String ad_id;
        private String family_id;
        private String time;
        private String position;

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getFamily_id() {
            return family_id;
        }

        public void setFamily_id(String family_id) {
            this.family_id = family_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }
}
