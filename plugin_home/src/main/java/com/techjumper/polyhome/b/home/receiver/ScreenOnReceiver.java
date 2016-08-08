package com.techjumper.polyhome.b.home.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.techjumper.commonres.entity.event.MissReadEvent;
import com.techjumper.commonres.entity.event.TimerEvent;
import com.techjumper.corelib.rx.tools.RxBus;

/**
 * Created by kevin on 16/7/27.
 */

public class ScreenOnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       RxBus.INSTANCE.send(new TimerEvent(true));
    }
}
