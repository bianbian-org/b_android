package com.techjumper.polyhome_b.adlib.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/7/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdStatParamEntity {

    /**
     * ad_id : 10
     * count : 12
     */

    private List<AdsEntity> ads;

    public List<AdsEntity> getAds() {
        return ads;
    }

    public void setAds(List<AdsEntity> ads) {
        this.ads = ads;
    }

    public static class AdsEntity {
        private String ad_id;
        private String count;

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
