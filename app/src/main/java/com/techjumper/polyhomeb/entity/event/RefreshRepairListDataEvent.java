package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/6
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RefreshRepairListDataEvent {
    private int repairStatus;

    public int getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(int repairStatus) {
        this.repairStatus = repairStatus;
    }

    public RefreshRepairListDataEvent(int repairStatus) {
        this.repairStatus = repairStatus;
    }
}
