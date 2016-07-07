package com.techjumper.polyhome.b.home.tool.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.techjumper.commonres.entity.event.WeatherDateEvent;
import com.techjumper.commonres.entity.event.pushevent.NoticePushEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;

/**
 * Created by kevin on 16/6/21.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final int WEATHER = 0;
    public static final int NOTICES = 1;

    public static final String TYPE = "type";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(AlarmManagerUtil.TAG, "接受消息");

        if (intent.getIntExtra(TYPE, WEATHER) == WEATHER) {
            Log.d(AlarmManagerUtil.TAG, "执行天气请求......");
            if (UserInfoManager.isLogin()) {
                RxBus.INSTANCE.send(new WeatherDateEvent());
            }
        } else {
            Log.d(AlarmManagerUtil.TAG, "执行通知请求......");
            if (UserInfoManager.isLogin()) {
                RxBus.INSTANCE.send(new NoticePushEvent());
            }
        }
    }
}
