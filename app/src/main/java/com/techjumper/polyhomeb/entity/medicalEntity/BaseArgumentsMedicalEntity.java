package com.techjumper.polyhomeb.entity.medicalEntity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class BaseArgumentsMedicalEntity {

    private int devicetype;
    private int logintype;

    public BaseArgumentsMedicalEntity(int devicetype, int logintype) {
        this.devicetype = devicetype;
        this.logintype = logintype;
    }

    public int getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }

    public int getLogintype() {
        return logintype;
    }

    public void setLogintype(int logintype) {
        this.logintype = logintype;
    }
}
