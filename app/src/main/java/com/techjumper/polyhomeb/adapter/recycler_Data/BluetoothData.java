package com.techjumper.polyhomeb.adapter.recycler_Data;

import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class BluetoothData {

    private List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infosBeen;
    private boolean isCommunitySupportBleDoor;

    public boolean isCommunitySupportBleDoor() {
        return isCommunitySupportBleDoor;
    }

    public void setCommunitySupportBleDoor(boolean communitySupportBleDoor) {
        isCommunitySupportBleDoor = communitySupportBleDoor;
    }

    public List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> getInfosBeen() {
        return infosBeen;
    }

    public void setInfosBeen(List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infosBeen) {
        this.infosBeen = infosBeen;
    }
}
