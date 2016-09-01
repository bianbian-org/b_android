package com.techjumper.polyhome.b.property.mvp.p.activity;

import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.HeartbeatEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.SchedulersCompat;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/12.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    private int backType = BackEvent.PROPERTY_ACTION;
    private long time;

    @OnClick(R.id.bottom_back)
    void back() {
        if (backType == BackEvent.PROPERTY_ACTION) {
            RxBus.INSTANCE.send(new PropertyListEvent());
        } else if (backType == BackEvent.PROPERTY_LIST) {
            RxBus.INSTANCE.send(new PropertyListEvent());
        } else {
            getView().finish();
        }
    }

    @OnClick(R.id.bottom_home)
    void home() {
        getView().finish();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        time = getView().getTime();

        if (time == 0L) {
            time = System.currentTimeMillis() / 1000;
        }
        getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof BackEvent) {
                        BackEvent backEvent = (BackEvent) o;
                        backType = backEvent.getType();
                    } else if (o instanceof TimeEvent) {
                        Log.d("time", "更新时间");
                        time++;
                        if (getView().getBottomDate() != null) {
                            String second = CommonDateUtil.getSecond(time);
                            Log.d("prosubmitOnline", "second: " + second);
                            if (second.equals("00")) {
                                getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                            }
                        }
                    }else if (o instanceof HeartbeatEvent) {
                        HeartbeatEvent event = (HeartbeatEvent) o;
                        if (event != null) {
                            time = event.getTime();
                            Log.d("prosubmitOnline", "心跳时间:" + time);
                            getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                        }
                    }
                }));
    }
}
