package me.iwf.photopicker.event;

import android.content.Intent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class PhotoPickerEvent {

    private Intent intent;

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public PhotoPickerEvent(Intent intent) {
        this.intent = intent;
    }

}
