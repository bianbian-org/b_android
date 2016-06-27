package com.techjumper.polyhome.b.home.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.techjumper.commonres.entity.event.pushevent.NoticePushEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;

/**
 * Created by kevin on 16/6/24.
 */
public class NoticeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        Bundle bundle = intent.getExtras();
        ToastUtils.show(bundle.get("key_extra").toString());

        RxBus.INSTANCE.send(new NoticePushEvent());
    }
}
