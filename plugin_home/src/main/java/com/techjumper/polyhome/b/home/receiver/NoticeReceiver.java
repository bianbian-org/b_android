package com.techjumper.polyhome.b.home.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.entity.event.AdEvent;
import com.techjumper.commonres.entity.event.pushevent.NoticePushEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;

/**
 * Created by kevin on 16/6/24.
 */
public class NoticeReceiver extends BroadcastReceiver {

    public static final String TAG = "NoticeReceiver";
    public static final String AD = "advertisement";
    public static final String NOTICE = "notice";
    public static final String UPDATENOTICE = "updateNotice";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        Bundle bundle = intent.getExtras();
        String type = bundle.get("key_extra").toString();
        Log.d(TAG, type);

        if (type.equals(AD)) {
            ToastUtils.show("收到推送消息：adverstiment");
            RxBus.INSTANCE.send(new AdEvent());
        } else if (type.equals(NOTICE)) {
            ToastUtils.show("收到推送消息：notice");
            RxBus.INSTANCE.send(new NoticePushEvent());
        } else if (type.equals(UPDATENOTICE)) {
            ToastUtils.show("收到推送消息：updateNotice");
            RxBus.INSTANCE.send(new NoticePushEvent());
        }
    }
}
