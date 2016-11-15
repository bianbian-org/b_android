package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.MedicalEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.entity.event.AdTemEvent;
import com.techjumper.commonres.entity.event.HeartbeatTimeEvent;
import com.techjumper.commonres.entity.event.InfoMediaPlayerEvent;
import com.techjumper.commonres.entity.event.MedicalEvent;
import com.techjumper.commonres.entity.event.MissReadEvent;
import com.techjumper.commonres.entity.event.ShowMainAdEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.WeatherDateEvent;
import com.techjumper.commonres.entity.event.WeatherEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.commonres.util.RxUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.adapter.AdViewPagerAdapter;
import com.techjumper.polyhome.b.home.db.util.AdClickDbUtil;
import com.techjumper.polyhome.b.home.mvp.m.InfoFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdNewActivity;
import com.techjumper.polyhome.b.home.mvp.v.fragment.InfoFragment;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;
import com.techjumper.polyhome.b.home.widget.AdViewPager;
import com.techjumper.polyhome.b.home.widget.ArcDataView;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.manager.AdController;
import com.techjumper.polyhome_b.adlib.window.AdWindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/27.
 */
public class InfoFragmentPresenter extends AppBaseFragmentPresenter<InfoFragment> implements AdController.IAlarm, ViewPager.OnPageChangeListener {
    private InfoFragmentModel infoFragmentModel = new InfoFragmentModel(this);
    private AdController adController;
    private MyTextureView textureView;
    private ImageView adImageView;
    private AdEntity.AdsEntity mAdsEntity = new AdEntity.AdsEntity();
    private String addType = AdViewPagerAdapter.IMAGE_AD_TYPE;
    private List<MedicalEntity.MedicalItemEntity> entities = new ArrayList<>();
    private ArcDataView advHeartrate;
    private ArcDataView advBloodsugar;
    private ArcDataView advBloodpressure;
    private ArcDataView advDetect;
    private int videoPosition = 0;
    private int medicalPosition = 0;
    private boolean mIsVisibleToUser;
    private boolean mIsGetAd;
    private boolean mIsGetNewAd;
    private long heartbeatTime;
    private AdViewPager adViewPager;

    private AdViewPagerAdapter adapter = new AdViewPagerAdapter();
    private List<View> views = new ArrayList<>();
    private List<Integer> resIds = new ArrayList<>();
    private int currentPage = 0;
    private LayoutInflater inflater;
    private View currentView;
    private float x1, x2, y1, y2;

    private List<AdEntity.AdsEntity> adsEntities = new ArrayList<>();

    @OnClick(R.id.info_arrow_layout)
    void changeMedicalInfo() {
        Log.d("medical: ", "entities.size()" + entities.size());

        if (entities != null && entities.size() > 1) {
            if (medicalPosition == entities.size() - 1) {
                medicalPosition = 0;
            } else {
                medicalPosition++;
            }
            MedicalEntity.MedicalItemEntity itemEntity = entities.get(medicalPosition);
            Log.d("medical", "itemEntity: " + itemEntity);
            advHeartrate.setContentText(itemEntity.getHeartRate());
            advBloodsugar.setContentText(itemEntity.getBgValue());
            advBloodpressure.setContentText(itemEntity.getBpValue());
            advDetect.setContentText(itemEntity.getName());
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getView().getContext());

        adController = AdController.getInstance();

        advHeartrate = getView().getAdvHeartrate();
        advBloodsugar = getView().getAdvBloodsugar();
        advBloodpressure = getView().getAdvBloodpressure();
        advDetect = getView().getAdvDetect();
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        adImageView = getView().getImageAdTem();
        textureView = getView().getTextureViewTem();
        adViewPager = getView().getAdvp();

        adViewPager.setAdapter(adapter);
        adViewPager.setOffscreenPageLimit(3);
        adViewPager.addOnPageChangeListener(this);
        adViewPager.setCurrentItem(0);

        getWeatherInfo();
        getCalendarInfo();

        AlarmManagerUtil.setWeatherTime(Utils.appContext);

        addSubscription(RxView.clicks(getView().getSetting())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    Intent it = new Intent();
                    ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.setting.activity.SettingActivity");
                    it.setComponent(componentName);
                    getView().startActivity(it);
                }));

        addSubscription(RxView.clicks(getView().getAdvDetect())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    PluginEngineUtil.startMedical();
                }));
        addSubscription(RxView.clicks(getView().getAdvHeartrate())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    PluginEngineUtil.startMedical();
                }));
        addSubscription(RxView.clicks(getView().getAdvBloodsugar())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    PluginEngineUtil.startMedical();
                }));
        addSubscription(RxView.clicks(getView().getAdvBloodpressure())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    PluginEngineUtil.startMedical();
                }));

        addSubscription(RxView.clicks(getView().getSpeak())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    Intent it = new Intent();
                    ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.CallingActivity");
                    it.setComponent(componentName);
                    getView().startActivity(it);
                }));

        addSubscription(RxView.clicks(getView().getAdTem())
                .filter(aVoid -> {
                    if (mAdsEntity != null && !TextUtils.isEmpty(mAdsEntity.getMedia_type()))
                        return true;

                    return false;
                })
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    AdClickDbUtil.insert(Long.valueOf(mAdsEntity.getId()), AdController.TYPE_HOME_TWO, ComConstant.AD_TYPE_CLICK, heartbeatTime);
                    Intent intent = new Intent(getView().getActivity(), AdActivity.class);
                    intent.putExtra(AdActivity.ADITEM, mAdsEntity);
                    getView().getActivity().startActivity(intent);
                }));

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof WeatherDateEvent) {
                        getWeatherInfo();
                        getCalendarInfo();
                    }
                }));

        addSubscription(RxBus.INSTANCE.asObservable()
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
                        AdTemEvent event = (AdTemEvent) o;
                        mIsGetNewAd = event.isFromCahce();
                        Log.d("mIsGetNewAd", "AdTemEvent mIsGetNewAd " + mIsGetNewAd + "");
                        if (mIsGetAd) {
                            getNormalAd(mIsGetNewAd);
                        }
                    } else if (o instanceof MedicalEvent) {
                        MedicalEvent event = (MedicalEvent) o;
                        Log.d("pluginUserInfo", "收到的MedicalEvent: " + event);

                        if (entities == null) {
                            entities = new ArrayList<MedicalEntity.MedicalItemEntity>();
                        } else if (entities.size() > 0) {
                            entities.clear();
                        }

                        entities = event.getEntities();
                        medicalPosition = 0;

                        Log.d("pluginUserInfo", "收到的entities: " + entities);

                        if (entities != null && entities.size() > 0) {
                            MedicalEntity.MedicalItemEntity itemEntity = entities.get(medicalPosition);
                            Log.d("pluginUserInfo", "显示的第一个人名字为" + itemEntity.getName());
                            advHeartrate.setContentText(itemEntity.getHeartRate());
                            Log.d("pluginUserInfo", "advHeartrate 显示内容为" + advHeartrate.getContentText());
                            advBloodsugar.setContentText(itemEntity.getBgValue());
                            Log.d("pluginUserInfo", "advBloodsugar 显示内容为" + advBloodsugar.getContentText());
                            advBloodpressure.setContentText(itemEntity.getBpValue());
                            Log.d("pluginUserInfo", "advBloodpressure 显示内容为" + advBloodpressure.getContentText());
                            advDetect.setContentText(itemEntity.getName());
                            Log.d("pluginUserInfo", "advDetect 显示内容为" + advDetect.getContentText());
                            Log.d("pluginUserInfo", "有数据并且执行完毕" + itemEntity.getName());
                        } else {
                            if (entities != null) {
                                entities.clear();
                            }
                            Log.d("pluginUserInfo", "未登录");
                            advHeartrate.setContentText(getView().getString(R.string.info_medical_default));
                            advBloodsugar.setContentText(getView().getString(R.string.info_medical_default));
                            advBloodpressure.setContentText(getView().getString(R.string.info_medical_default));
                            advDetect.setContentText(getView().getString(R.string.info_medical_no_login));
                        }
                    } else if (o instanceof ShowMainAdEvent) {
                        if (mIsGetAd) {
                            getNormalAd(true);
                        }
                    } else if (o instanceof MissReadEvent) {
                        int num = ((MissReadEvent) o).getNum();
                        getView().getSpeak().showQuarterText(String.valueOf(num));
                    } else if (o instanceof InfoMediaPlayerEvent) {
                        InfoMediaPlayerEvent event = (InfoMediaPlayerEvent) o;
                        if (event.getType() == InfoMediaPlayerEvent.INFO) {
                            if (textureView != null) {
                                Log.d("ergou", "initMediaPlayer");
                                textureView.initMediaPlayer();
                            }
                        }
                    } else if (o instanceof HeartbeatTimeEvent) {
                        HeartbeatTimeEvent event = (HeartbeatTimeEvent) o;
                        heartbeatTime = event.getTime();
                    }
                }));

        adViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("ad12", "关掉");
                        adController.stopAdTimer(AdController.TYPE_HOME);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("ad12", "开启");
                        Log.d("ad12", x1 + " " + x2);
                        x2 = event.getX();
                        y2 = event.getY();
                        if (Math.abs(x1 - x2) < 6 && Math.abs(y1 - y2) < 6) {
                            AdClickDbUtil.insert(Long.valueOf(mAdsEntity.getId()), AdController.TYPE_HOME, ComConstant.AD_TYPE_CLICK, heartbeatTime);
                            Intent intent = new Intent(getView().getActivity(), AdNewActivity.class);
                            intent.putExtra(AdNewActivity.POSITION, adViewPager.getCurrentItem());
                            intent.putExtra(AdNewActivity.TYPE, AdNewActivity.TYPE_TWO);
                            intent.putExtra(AdNewActivity.TIME, heartbeatTime);
                            getView().getActivity().startActivity(intent);
                        }
                        adController.startAdTimer(AdController.TYPE_HOME, currentPage);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsVisibleToUser) {
            if (adController != null) {
                adController.cancel(AdController.TYPE_HOME_TWO);
            }
            initAd();
            mIsGetAd = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            Log.d("hehe", "info显示");
            Log.d("mIsGetNewAd", mIsGetNewAd + "");
            getNormalAd(mIsGetNewAd);
            mIsGetAd = true;
        } else {
            Log.d("hehe", "info消失");
            if (adController != null) {
                adController.cancel(AdController.TYPE_HOME_TWO);
            }
            initAd();
            mIsGetAd = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsVisibleToUser) {
            if (textureView != null) {
                Log.d("ergou", "initMediaPlayer");
                textureView.initMediaPlayer();
            }
            RxBus.INSTANCE.send(new InfoMediaPlayerEvent(InfoMediaPlayerEvent.PLOY));
            Log.d("mIsGetNewAd", mIsGetNewAd + "");
            getNormalAd(mIsGetNewAd);
            mIsGetAd = true;
        }
    }

    //获取天气相关
    private void getWeatherInfo() {
        Log.d("weatherDate", "weather enter");
        Log.d("weatherDate", "familyId" + UserInfoManager.getFamilyId() == null ? "null" : UserInfoManager.getFamilyId());
        Log.d("weatherDate", "ticket" + UserInfoManager.getTicket() == null ? "null" : UserInfoManager.getTicket());

        if (!UserInfoManager.isLogin())
            return;

        Log.d("weatherDate", "weather begin");

        addSubscription(infoFragmentModel.getWeatherInfo()
                .subscribe(new Subscriber<WeatherEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
//                        if (!TextUtils.isEmpty(e.toString())) {
//                            ToastUtils.showLong(e.toString());
//                        }
                    }

                    @Override
                    public void onNext(WeatherEntity weatherEntity) {
                        if (!processNetworkResult(weatherEntity, false))
                            return;

                        if (weatherEntity != null && weatherEntity.getData() != null)
                            getView().getWeatherInfo(weatherEntity.getData());

//                        ToastUtils.showLong("weatherEntity data" + weatherEntity.getData());

                        Log.d("weatherDate", "weather data" + weatherEntity.getData());
                        //发送给主页获取数据
                        RxBus.INSTANCE.send(new WeatherEvent(weatherEntity.getData()));
                    }
                }));
    }

    //获取日历相关
    private void getCalendarInfo() {
        Log.d("weatherDate", "date begin");

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

                        Log.d("weatherDate", "date data" + calendarEntity.getData());
                    }
                }));
    }

    /**
     * 获取普通广告
     */
    private void getNormalAd(boolean fromCahce) {
        Log.d("isShow", AdWindowManager.getInstance().isShow() + "");

        if (AdWindowManager.getInstance().isShow())
            return;

        if (!UserInfoManager.isLogin() || adController == null)
            return;

        Log.d("hehe", "info普通获取广告");
        adController.startPolling(this, fromCahce);
    }

    /**
     * 初始化广告
     */
    private void initAd() {
        if (adImageView == null || textureView == null)
            return;

        if (adapter != null && adViewPager != null) {
            Log.d("ad12", "清除");
            adViewPager.removeAllViews();
            adViewPager.setAdapter(adapter = new AdViewPagerAdapter());
            currentPage = 0;
        }

        textureView.stop();
        adImageView.setVisibility(View.VISIBLE);
        textureView.setVisibility(View.INVISIBLE);
        adImageView.setBackgroundResource(R.mipmap.bg_ad);
        adImageView.setImageBitmap(null);
        mAdsEntity = null;
    }

    /**
     * 处理广告
     */
    private void HandleAd(AdEntity.AdsEntity adsEntity, File file) {
        JLog.d("有新的广告来啦. 本地广告路径:" + file + ", 详细信息: " + adsEntity);
        addType = adsEntity.getMedia_type();

        if (addType.equals(AdViewPagerAdapter.IMAGE_AD_TYPE)) {
            adImageView.setVisibility(View.VISIBLE);
            textureView.setVisibility(View.INVISIBLE);

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

            adsEntity.setMedia_url(file.getAbsolutePath());
        } else if (addType.equals(AdViewPagerAdapter.VIDEO_AD_TYPE)) {
            Log.d("ergou", "equals");
            adImageView.setVisibility(View.INVISIBLE);
            textureView.setVisibility(View.VISIBLE);
            Log.d("ergou", "play");
            if (file.exists()) {
                textureView.play(file.getAbsolutePath());
            } else {
                textureView.play(adsEntity.getMedia_url());
            }
            adsEntity.setMedia_url(file.getAbsolutePath());
        }
        mIsGetNewAd = true;
        mAdsEntity = adsEntity;
    }

    @Override
    public void onAlarmReceive(boolean fromCache) {
        JLog.d("普通获取广告" + UserInfoManager.getFamilyId() + "  "
                + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket() + " fromCache=" + fromCache);
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
        adController.executeAdRule(AdController.TYPE_HOME_TWO, UserInfoManager.getFamilyId()
                , UserInfoManager.getUserId(), UserInfoManager.getTicket()
                , fromCache
                , new AdController.IExecuteRule() {
                    @Override
                    public void onAllAdsReceive(List<AdEntity.AdsEntity> allAds) {
                        if (allAds == null || allAds.size() == 0)
                            return;

                        if (views != null && views.size() > 0) {
                            views.clear();
                        }

                        if (adsEntities != null && adsEntities.size() > 0) {
                            adsEntities.clear();
                        }

                        Log.d("ad11", "所有" + allAds);

                        for (int i = 0; i < allAds.size(); i++) {
                            AdEntity.AdsEntity entity = allAds.get(i);
                            File file = entity.getFile();
                            if (file.exists()) {
                                adsEntities.add(entity);
                                Log.d("ad12", file + ", 详细信息: " + entity);
                                addType = entity.getMedia_type();

                                if (adImageView == null || textureView == null)
                                    return;
                                if (AdViewPagerAdapter.IMAGE_AD_TYPE.equals(addType)) {
//                                    ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_ad_image, null);
                                    View imageView = new Space(getView().getContext());

                                    views.add(imageView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                } else if (AdViewPagerAdapter.VIDEO_AD_TYPE.equals(addType)) {
//                                    MyTextureView textureView = (MyTextureView) inflater.inflate(R.layout.layout_ad_video, null);
                                    View textureView = new Space(getView().getContext());

                                    views.add(textureView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                }
                            }
                        }
//                        adapter.setViews(views, adsEntities);
//                        adapter.setDatas(adsEntities);
                        if (adViewPager == null)
                            return;
                        adapter = new AdViewPagerAdapter();
                        adapter.setDatas(adsEntities);
                        adViewPager.setAdapter(adapter);
//                        if (adViewPager.getWrapper() != null) {
//                            adViewPager.getWrapper().notifyDataSetChanged();
//                        }
//                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
//                        HandleAd(adsEntity, file);
                        Log.d("ad12", "跳下一页, 当前页" + currentPage);
                        if (adViewPager != null) {
                            adViewPager.setCurrentItem(currentPage, false, true);
                        }
                        mIsGetNewAd = true;
                        mAdsEntity = adsEntity;

                        if (currentPage == views.size() - 1) {
                            currentPage = -1;
                        }
                        currentPage++;
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("ad12", "滑动到当前页" + position);
        if (views.size() != 0
                && adsEntities.size() != 0
                && views.size() == adsEntities.size()) {
            mAdsEntity = adsEntities.get(position);
            if (mAdsEntity.getMedia_type().equals(AdViewPagerAdapter.VIDEO_AD_TYPE)) {
                if (currentView != null) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
                currentView = adViewPager.findViewWithTag(position);

                if (currentView == null)
                    return;

                adapter.playVideo(currentView, mAdsEntity.getFile());
            } else {
                if (currentView != null && currentView instanceof MyTextureView) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
            }
        }
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("scoll", state + "");
        if (state == 2 && mAdsEntity != null) {
            AdClickDbUtil.insert(Long.valueOf(mAdsEntity.getId()), AdController.TYPE_HOME, ComConstant.AD_TYPE_SLIDE, heartbeatTime);
        }
    }
}
