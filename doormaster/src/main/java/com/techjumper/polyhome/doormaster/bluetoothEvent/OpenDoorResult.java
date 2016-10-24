package com.techjumper.polyhome.doormaster.bluetoothEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class OpenDoorResult {

    private boolean result;

    public boolean isResult() {
        return result;
    }

    public OpenDoorResult(boolean result) {

        this.result = result;
    }
}
