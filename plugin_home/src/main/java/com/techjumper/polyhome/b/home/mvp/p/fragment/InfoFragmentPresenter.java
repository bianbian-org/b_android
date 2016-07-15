package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.MedicalEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.entity.event.AdTemEvent;
import com.techjumper.commonres.entity.event.MedicalEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.WeatherDateEvent;
import com.techjumper.commonres.entity.event.WeatherEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.m.InfoFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.fragment.InfoFragment;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;
import com.techjumper.polyhome.b.home.widget.ArcDataView;
import com.techjumper.polyhome.b.home.widget.MyVideoView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.manager.AdController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/27.
 */
public class InfoFragmentPresenter extends AppBaseFragmentPresenter<InfoFragment> {
    private InfoFragmentModel infoFragmentModel = new InfoFragmentModel(this);
    private AdController adController;
    private MyVideoView video;
    private ImageView adImageView;
    private AdEntity.AdsEntity mAdsEntity = new AdEntity.AdsEntity();
    private String addType = PloyhomeFragmentPresenter.IMAGE_AD_TYPE;
    private List<MedicalEntity.MedicalItemEntity> entities = new ArrayList<>();
    private ArcDataView advHeartrate;
    private ArcDataView advBloodsugar;
    private ArcDataView advBloodpressure;
    private ArcDataView advDetect;

    private int medicalPosition = 0;

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

    @OnClick(R.id.info_arrow_layout)
    void changeMedicalInfo() {
        if (entities != null && entities.size() > 1) {
            if (medicalPosition == entities.size() - 1) {
                medicalPosition = 0;
            } else {
                medicalPosition++;
            }
            MedicalEntity.MedicalItemEntity itemEntity = entities.get(medicalPosition);

            advHeartrate.setContentText(itemEntity.getHeartRate());
            advBloodsugar.setContentText(itemEntity.getBgValue());
            advBloodpressure.setContentText(itemEntity.getBpValue());
            advDetect.setContentText(itemEntity.getName());
        }
    }

    @OnClick(R.id.speak)
    void speak() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.CallingActivity");
        it.setComponent(componentName);
        getView().startActivity(it);
    }

    @OnClick(R.id.ad_tem)
    void ad_tem() {
        if (TextUtils.isEmpty(mAdsEntity.getMedia_type()))
            return;

        Intent intent = new Intent(getView().getActivity(), AdActivity.class);
        intent.putExtra(AdActivity.ADITEM, mAdsEntity);
        getView().getActivity().startActivity(intent);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        adController = new AdController(getView().getActivity());

        advHeartrate = getView().getAdvHeartrate();
        advBloodsugar = getView().getAdvBloodsugar();
        advBloodpressure = getView().getAdvBloodpressure();
        advDetect = getView().getAdvDetect();
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        adImageView = getView().getImageAdTem();
        video = getView().getVideoAdTem();

        if (UserInfoManager.isLogin()) {
            getWeatherInfo();
        }
        getCalendarInfo();

//        postMedical();

        AlarmManagerUtil.setWeatherTime(Utils.appContext, 0, 30 + new Random().nextInt(30));

        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof WeatherDateEvent) {
                        getWeatherInfo();
                        getCalendarInfo();
                    }
                });

        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof UserInfoEvent) {
                        UserInfoEvent event = (UserInfoEvent) o;
                        if (event != null && event.getEntity() != null) {
                            Log.d("pluginUserInfo", "更新天气信息");
                            getWeatherInfo();
                            getCalendarInfo();
                        }
                    } else if (o instanceof AdTemEvent) {
                        getNormalAd();
                    } else if (o instanceof MedicalEvent) {
                        MedicalEvent event = (MedicalEvent) o;
                        entities = event.getEntities();
                        if (entities != null && entities.size() > 0) {
                            MedicalEntity.MedicalItemEntity itemEntity = entities.get(medicalPosition);

                            advHeartrate.setContentText(itemEntity.getHeartRate());
                            advBloodsugar.setContentText(itemEntity.getBgValue());
                            advBloodpressure.setContentText(itemEntity.getBpValue());
                            advDetect.setContentText(itemEntity.getName());
                        }
                    }
                });

    }

    //获取天气相关
    private void getWeatherInfo() {
        if (!UserInfoManager.isLogin())
            return;

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
                        if (!processNetworkResult(weatherEntity, false))
                            return;

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
                        if (!processNetworkResult(calendarEntity, false))
                            return;

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

    /**
     * 获取普通广告
     */
    private void getNormalAd() {
        adController.startPolling(new AdController.IAlarm() {
            @Override
            public void onAlarmReceive() {
                JLog.d("普通获取广告" + UserInfoManager.getFamilyId() + "  " + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket());
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
                adController.executeAdRule(AdController.TYPE_HOME, UserInfoManager.getFamilyId(), UserInfoManager.getUserId(), UserInfoManager.getTicket(), new AdController.IExecuteRule() {
                    @Override
                    public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                        HandleAd(adsEntity, file);
                    }

                    @Override
                    public void onAdPlayFinished() {
                        JLog.d("广告播放完成  (有可能是上一次的任务被自动中断，不影响本次广告执行)");
                        initAd();
                    }

                    @Override
                    public void onAdDownloadError(AdEntity.AdsEntity adsEntity) {
                        JLog.d("某个广告下载失败: " + adsEntity);
                    }

                    @Override
                    public void onAdExecuteFailed(String reason) {
                        JLog.d("获取广告失败: " + reason);
                        initAd();
                    }

                    @Override
                    public void onAdNoExist(String adType, String hour) {
                        JLog.d("没有广告: 广告类型=" + adType + ", 当前小时=" + hour);
                        initAd();
                    }
                });
            }
        });
    }

    /**
     * 初始化广告
     */
    private void initAd() {
        adImageView.setVisibility(View.VISIBLE);
        video.setVisibility(View.INVISIBLE);
        adImageView.setBackgroundResource(R.mipmap.bg_ad);
        adImageView.setImageBitmap(null);
    }

    /**
     * 处理广告
     */
    private void HandleAd(AdEntity.AdsEntity adsEntity, File file) {
        JLog.d("有新的广告来啦. 本地广告路径:" + file + ", 详细信息: " + adsEntity);
        addType = adsEntity.getMedia_type();

        if (addType.equals(PloyhomeFragmentPresenter.IMAGE_AD_TYPE)) {
            adImageView.setVisibility(View.VISIBLE);
            video.setVisibility(View.INVISIBLE);

            if (file.exists()) {
                PicassoHelper.load(file)
                        .noPlaceholder()
                        .noFade()
                        .into(adImageView);
            } else {
                PicassoHelper.load(adsEntity.getMedia_url())
                        .noPlaceholder()
                        .noFade()
                        .into(adImageView);
            }

            adsEntity.setMedia_url(file.getAbsolutePath().toString());
        } else if (addType.equals(PloyhomeFragmentPresenter.VIDEO_AD_TYPE)) {

            adImageView.setVisibility(View.INVISIBLE);
            video.setVisibility(View.VISIBLE);
            if (file.exists()) {
                video.setVideoPath(file.getAbsolutePath().toString());
            } else {
                video.setVideoURI(Uri.parse(adsEntity.getMedia_url()));
            }

            video.start();
            video.requestFocus();

            adsEntity.setMedia_url(file.getAbsolutePath().toString());
        }

        mAdsEntity = adsEntity;
    }
}
