package com.techjumper.polyhome.b.home.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.techjumper.commonres.entity.event.MissReadEvent;
import com.techjumper.commonres.util.RxUtil;
import com.techjumper.corelib.rx.tools.RxBus;

/**
 * Created by kevin on 16/7/27.
 */

public class MissReadReceiver extends BroadcastReceiver {

    private int miss_num = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String a = intent.getAction();
        if (a.equals("com.dnake.broadcast")) {
            String e = intent.getStringExtra("event");
            if (e.equals("com.dnake.talk.data")) {
                miss_num = intent.getIntExtra("miss", 0);
                RxBus.INSTANCE.send(new MissReadEvent(miss_num));
            }
        }
    }
}
