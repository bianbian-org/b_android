package com.techjumper.polyhome.doormaster.bluetoothEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class BLEScanResultEvent {

    private String sn;
    private boolean hasDevice;

    public BLEScanResultEvent(boolean hasDevice,String sn) {
        this.sn = sn;
        this.hasDevice = hasDevice;
    }

    public boolean isHasDevice() {
        return hasDevice;
    }

    public String getSn() {
        return sn;
    }

}
