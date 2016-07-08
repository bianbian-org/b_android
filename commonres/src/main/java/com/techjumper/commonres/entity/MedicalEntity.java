package com.techjumper.commonres.entity;

/**
 * 医疗信息
 * Created by kevin on 16/7/7.
 */

public class MedicalEntity {

    private String bgValue;
    private String bpValue;
    private String heartRate;
    private String name;

    public String getBgValue() {
        return bgValue;
    }

    public void setBgValue(String bgValue) {
        this.bgValue = bgValue;
    }

    public String getBpValue() {
        return bpValue;
    }

    public void setBpValue(String bpValue) {
        this.bpValue = bpValue;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
