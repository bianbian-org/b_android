package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.plugincommunicateengine.Constants;
import com.techjumper.plugincommunicateengine.HostDataBuilder;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.m.InfoFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.fragment.InfoFragment;

import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by kevin on 16/4/27.
 */
public class InfoFragmentPresenter extends AppBaseFragmentPresenter<InfoFragment> {
    private InfoFragmentModel infoFragmentModel = new InfoFragmentModel(this);

    @OnClick(R.id.setting)
    void setting() {
        PluginEngineUtil.startSetting();
    }

    @OnClick(R.id.detect_layout)
    void detect() {
        PluginEngineUtil.startMedical();
    }

    @OnClick(R.id.speak)
    void speak() {
        PluginEngineUtil.startTalk();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getWeatherInfo();
        getCalendarInfo();
    }

    private void getWeatherInfo() {
        addSubscription(infoFragmentModel.getWeatherInfo()
                .subscribe(new Subscriber<WeatherEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WeatherEntity weatherEntity) {
                        if (weatherEntity != null && weatherEntity.getData() != null)
                            getView().getWeatherInfo(weatherEntity.getData());
                    }
                }));
    }

    private void getCalendarInfo() {
        addSubscription(infoFragmentModel.getCalendarInfo()
                .subscribe(new Subscriber<CalendarEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CalendarEntity calendarEntity) {
                        if (calendarEntity != null && calendarEntity.getData() != null)
                            getView().getCalendarInfo(calendarEntity.getData());
                    }
                }));
    }
}
