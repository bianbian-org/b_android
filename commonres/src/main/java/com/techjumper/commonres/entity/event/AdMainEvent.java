package com.techjumper.commonres.entity.event;

import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import java.io.File;

/**
 * 首页广告event
 * Created by kevin on 16/7/8.
 */

public class AdMainEvent {
    private AdEntity.AdsEntity adsEntity;
    private File file;

    public AdMainEvent(AdEntity.AdsEntity adsEntity, File file) {
        this.adsEntity = adsEntity;
        this.file = file;
    }

    public AdEntity.AdsEntity getAdsEntity() {
        return adsEntity;
    }

    public void setAdsEntity(AdEntity.AdsEntity adsEntity) {
        this.adsEntity = adsEntity;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
