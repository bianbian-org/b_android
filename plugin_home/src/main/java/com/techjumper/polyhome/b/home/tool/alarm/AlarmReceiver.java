package com.techjumper.polyhome.b.home.tool.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.techjumper.commonres.entity.event.WeatherDateEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.home.UserInfoManager;

/**
 * Created by kevin on 16/6/21.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (UserInfoManager.isLogin()) {
            RxBus.INSTANCE.send(new WeatherDateEvent());
        }
    }
}
