package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.WeatherDateEvent;
import com.techjumper.commonres.entity.event.WeatherEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.m.InfoFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.fragment.InfoFragment;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;

import java.util.Random;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/27.
 */
public class InfoFragmentPresenter extends AppBaseFragmentPresenter<InfoFragment> {
    private InfoFragmentModel infoFragmentModel = new InfoFragmentModel(this);

    @OnClick(R.id.setting)
    void setting() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.setting.activity.SettingActivity");
        it.setComponent(componentName);
        getView().startActivity(it);
    }

    @OnClick(R.id.detect_layout)
    void detect() {
        PluginEngineUtil.startMedical();
    }

    @OnClick(R.id.speak)
    void speak() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.CallingActivity");
        it.setComponent(componentName);
        getView().startActivity(it);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof UserInfoEvent) {
                        UserInfoEvent event = (UserInfoEvent) o;
                        if (event != null && event.getEntity() != null) {
                            getWeatherInfo();
                        }
                    }
                });
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
//        getWeatherInfo(429);
        getCalendarInfo();

        postMedical();

        AlarmManagerUtil.setTime(Utils.appContext, 0, new Random().nextInt(60));

        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof WeatherDateEvent) {
                        getWeatherInfo();
                        getCalendarInfo();
                    }
                });
    }

    //获取天气相关
    private void getWeatherInfo() {
        addSubscription(infoFragmentModel.getWeatherInfo()
                .subscribe(new Subscriber<WeatherEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(WeatherEntity weatherEntity) {
                        if (weatherEntity != null && weatherEntity.getData() != null)
                            getView().getWeatherInfo(weatherEntity.getData());
                        //发送给主页获取数据
                        RxBus.INSTANCE.send(new WeatherEvent(weatherEntity.getData()));
                    }
                }));
    }

    //获取日历相关
    private void getCalendarInfo() {
        addSubscription(infoFragmentModel.getCalendarInfo()
                .subscribe(new Subscriber<CalendarEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(CalendarEntity calendarEntity) {
                        if (calendarEntity != null && calendarEntity.getData() != null)
                            getView().getCalendarInfo(calendarEntity.getData());
                    }
                }));
    }

    //对医疗发送请求信息
    private void postMedical() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                try {
                    pluginExecutor.send(PluginEngine.CODE_CUSTOM, PluginConstant.ACTION_MEDICAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });

        PluginEngine.getInstance().registerReceiver((code, message, extras) -> {
            //接受医疗的消息
        });
    }
}
