package com.techjumper.polyhome.b.home.tool.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.techjumper.commonres.entity.event.AdClickEvent;
import com.techjumper.commonres.entity.event.SubmitOnlineClickEvent;
import com.techjumper.commonres.entity.event.WeatherDateEvent;
import com.techjumper.commonres.entity.event.pushevent.NoticePushEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;

/**
 * Created by kevin on 16/6/21.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final int WEATHER = 0;
    public static final int NOTICES = 1;
    public static final int ADCLICK = 2;
    public static final int SUBMITONLINE = 3;

    public static final String TYPE = "type";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(AlarmManagerUtil.TAG, "接受消息");

        if (intent.getIntExtra(TYPE, WEATHER) == WEATHER) {
            Log.d(AlarmManagerUtil.TAG, "执行天气请求......");
            if (UserInfoManager.isLogin()) {
                Log.d("tianqi", "执行天气请求......");
                AlarmManagerUtil.setWeatherTime(Utils.appContext);
                RxBus.INSTANCE.send(new WeatherDateEvent());
            }
        } else if (intent.getIntExtra(TYPE, WEATHER) == NOTICES) {
            AlarmManagerUtil.setNoticeTime(Utils.appContext);
            Log.d(AlarmManagerUtil.TAG, "执行通知请求......");
            if (UserInfoManager.isLogin()) {
                RxBus.INSTANCE.send(new NoticePushEvent());
            }
        } else if (intent.getIntExtra(TYPE, WEATHER) == ADCLICK) {
            Log.d(AlarmManagerUtil.TAG, "执行上传点击广告请求......");
            AlarmManagerUtil.setAdClick(Utils.appContext);
            RxBus.INSTANCE.send(new AdClickEvent());
        } else {
            Log.d(AlarmManagerUtil.TAG, "执行心跳包......");
            AlarmManagerUtil.setSubmitOnlineClick(Utils.appContext);
            RxBus.INSTANCE.send(new SubmitOnlineClickEvent());
        }
    }
}
