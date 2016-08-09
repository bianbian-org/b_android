package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.MedicalEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.AdClickEvent;
import com.techjumper.commonres.entity.event.AdControllerEvent;
import com.techjumper.commonres.entity.event.AdMainEvent;
import com.techjumper.commonres.entity.event.AdShowEvent;
import com.techjumper.commonres.entity.event.MedicalEvent;
import com.techjumper.commonres.entity.event.ShowMainAdEvent;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.plugincommunicateengine.IPluginMessageReceiver;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.home.BuildConfig;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.db.util.AdClickDbUtil;
import com.techjumper.polyhome.b.home.mvp.m.MainActivityModel;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;
import com.techjumper.polyhome.b.home.widget.MyVideoView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.manager.AdController;
import com.techjumper.polyhome_b.adlib.window.AdWindowManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/29.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    private MainActivityModel mainActivityModel = new MainActivityModel(this);
    private FrameLayout mainAdLayout;
    private LinearLayout mainContentLayout;
    private MyVideoView mainAdVideo;
    private ImageView mainAdImg;
    private Subscription submitOnlineSubscription;
    public static final String ACTION_START_HOST_DAEMON = "action_start_host_daemon";

    private IPluginMessageReceiver mIPluginMessageReceiver = (code, message, extras) -> {

        if (code == PluginEngine.CODE_GET_SAVE_INFO) {
            Log.d("pluginUserInfo", "开始从本地抓取用户信息数据...");
            Log.d("pluginUserInfo", "message: " + message);
            SaveInfoEntity saveInfoEntity = GsonUtils.fromJson(message, SaveInfoEntity.class);
            if (saveInfoEntity == null || saveInfoEntity.getData() == null)
                return;

            Log.d("pluginUserInfo", "name: " + saveInfoEntity.getData().getName());
            HashMap<String, String> hashMap = saveInfoEntity.getData().getValues();
            Log.d("pluginUserInfo", "hashMap: " + hashMap);
            if (hashMap == null)
                return;

            Log.d("pluginUserInfo", "hashMapSize: " + hashMap.size());
            if (hashMap == null || hashMap.size() == 0)
                return;

            String name = saveInfoEntity.getData().getName();

            if (name.equals(ComConstant.FILE_FAMILY_REGISTER)) {
                UserInfoEntity userInfoEntity = new UserInfoEntity();

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    Log.d("pluginUserInfo", entry.getValue());
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        userInfoEntity.setId(Long.parseLong(value));
                    } else if (key.equals("family_name")) {
                        userInfoEntity.setFamily_name(value);
                    } else if (key.equals("user_id")) {
                        userInfoEntity.setUser_id(Long.parseLong(value));
                    } else if (key.equals("ticket")) {
                        userInfoEntity.setTicket(value);
                    } else if (key.equals("has_binding")) {
                        userInfoEntity.setHas_binding(Integer.parseInt(value));
                    }
                }

                UserInfoManager.saveUserInfo(userInfoEntity);
                submitOnline();
                RxBus.INSTANCE.send(new UserInfoEvent(userInfoEntity));
            } else if (name.equals(ComConstant.FILE_MEDICAL)) {
                MedicalEntity medicalEntity = GsonUtils.fromJson(message, MedicalEntity.class);
                Log.d("pluginUserInfo", "medicalEntity: " + (medicalEntity == null ? "" : medicalEntity));
                Log.d("pluginUserInfo", "medicalEntity.getData(): " + (medicalEntity.getData() == null ? "" : medicalEntity.getData()));
                Log.d("pluginUserInfo", "medicalEntity.getData().getValues(): " + (medicalEntity.getData().getValues() == null ? "" : medicalEntity.getData().getValues()));
                Log.d("pluginUserInfo", "medicalEntity.getData().getValues().getDatas(): " + (medicalEntity.getData().getValues().getDatas() == null ? "" : medicalEntity.getData().getValues().getDatas()));

                if (medicalEntity == null
                        || medicalEntity.getData() == null
                        || medicalEntity.getData().getValues() == null
                        || medicalEntity.getData().getValues().getDatas() == null)
                    return;
                MedicalEntity.MedicalDataItemEntity medicalDataItemEntity = GsonUtils.fromJson(medicalEntity.getData().getValues().getDatas(), MedicalEntity.MedicalDataItemEntity.class);
                if (medicalDataItemEntity != null) {
                    List<MedicalEntity.MedicalItemEntity> medicalItemEntities = medicalDataItemEntity.getPerson();
                    Log.d("pluginUserInfo", "medicalItemEntities: " + medicalItemEntities.size());
                    RxBus.INSTANCE.send(new MedicalEvent(medicalItemEntities));
                }
            }
            Log.d("pluginUserInfo", "更新完毕用户信息...");
        } else if (code == PluginEngine.CODE_SAVE_INFO) {
            Log.d("pluginUserInfo", "用户设置保存文件名为: " + (TextUtils.isEmpty(message) ? "没有文件" : message));

            if (TextUtils.isEmpty(message))
                return;

//                InfoManager.saveUserInfoFile(message);

            PluginEngineUtil.initUserInfo(message);
        } else if (code == PluginEngine.CODE_CUSTOM) {
            Log.d("pluginUserInfo", "CODE_CUSTOM...");
            Log.d("pluginUserInfo", "message: " + (TextUtils.isEmpty(message) ? "" : message) + "  extras: " + extras == null ? "" : extras.getBoolean("key_ismedical") + "");
            if (!TextUtils.isEmpty(message)) {
                if (message.equals("action_medical")) {
                    if (extras != null) {
                        boolean isMedical = extras.getBoolean("key_ismedical");
                        if (isMedical == true) {
                            PluginEngineUtil.getMedical();
                        }
                    }
                }
            }
        }
        AlarmManagerUtil.setNoticeTime(Utils.appContext);
    };

//    @OnClick(R.id.title_img)
//    void titleImg() {
//        if (ComConstant.titleFinish) {
//            getView().finish();
//        }
//    }

    @OnClick(R.id.title)
    void title() {
        if (ComConstant.titleUpdate) {
            PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
                @Override
                public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                    try {
                        pluginExecutor.send(PluginEngine.CODE_UPDATE_PLUGIN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onEngineDisconnected() {

                }
            });
        }
    }

    @OnClick(R.id.date)
    void getVersion() {
        ToastUtils.show("code: " + String.valueOf(BuildConfig.VERSION_CODE) + " name: " + BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.call_service)
    void callService() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.CallingActivity");
        it.setComponent(componentName);
        it.putExtra("com.dnake.talk", "CallingActivity");
        getView().startActivity(it);
    }

    @OnClick(R.id.vedio)
    void callVedio() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.VideoSurveillanceActivity");
        it.setComponent(componentName);
        getView().startActivity(it);
    }

    @OnClick(R.id.main_ad_layout)
    void finishAd() {
        RxBus.INSTANCE.send(new AdControllerEvent());
        isShowMainAd(false);
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        AdWindowManager.getInstance().setOnAdsClickListener(new AdWindowManager.ParentClickListener() {
            @Override
            public void onAdsClick(AdEntity.AdsEntity adsEntity, File file) {
                RxBus.INSTANCE.send(new AdControllerEvent());
                AdWindowManager.getInstance().closeWindow(true);
            }
        });
        AdWindowManager.getInstance().setOnWindowShowListener(new AdWindowManager.IAdWindow() {
            @Override
            public void onAdWindowShow() {
                //取消接收首页广告
                AdController.getInstance().cancel(AdController.TYPE_HOME);
            }

            @Override
            public void onAdWindowClose(boolean byUser) {
                if (byUser) {
//                    AdController.getInstance().resetSleepTime();
                    RxBus.INSTANCE.send(new ShowMainAdEvent());
                } else {
                    AdController.getInstance().turnOffScreen();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        PluginEngine.getInstance().quit();
        AdWindowManager.getInstance().unregisterClickListener();
        AdWindowManager.getInstance().unregisterWindowShowListener();
        super.onDestroy();
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        submitOnline();
        getView().sendBroadcast(new Intent(ACTION_START_HOST_DAEMON));
        JLog.d("通知bhost启动守护进程");
        mainAdLayout = getView().getMainAdLayout();
        mainContentLayout = getView().getMainContentLayout();
        mainAdVideo = getView().getMainAdVideo();
        mainAdImg = getView().getMainAdImg();

        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof TimeEvent) {
                        Log.d("time", "更新时间");
                        if (getView().getDate() != null) {
                            getView().getDate().setText(CommonDateUtil.getTitleDate());
                        }
                    } else if (o instanceof AdMainEvent) {
                        AdMainEvent event = (AdMainEvent) o;
                        if (event.getAdsEntity() != null && event.getFile() != null) {
                            String addType = event.getAdsEntity().getMedia_type();
                            if (addType.equals(PloyhomeFragmentPresenter.IMAGE_AD_TYPE)) {
                                AdWindowManager.getInstance().showImage(event.getAdsEntity(), event.getFile());
                            } else {
                                AdWindowManager.getInstance().showVideo(event.getAdsEntity(), event.getFile());
                            }
                        }
                    } else if (o instanceof AdShowEvent) {
                        AdShowEvent event = (AdShowEvent) o;
//                        isShowMainAd(event.isShow());
                        if (!event.isShow()) {
                            AdWindowManager.getInstance().closeWindow(false);
                        }
                    } else if (o instanceof AdClickEvent) {
                        submitClicks();
                    }
                });

//        PluginEngineUtil.getMedical();
//        UserInfoEntity userInfoEntity2 = new UserInfoEntity();
//        userInfoEntity2.setId(438);
//        userInfoEntity2.setUser_id(367);
//        userInfoEntity2.setTicket("0f9859826eeeed6d421c0a0982ee5b08b6c41809");
//        UserInfoManager.saveUserInfo(userInfoEntity2);
//        RxBus.INSTANCE.send(new UserInfoEvent(userInfoEntity2));

//        if (!TextUtils.isEmpty(InfoManager.getUserInfoFile())) {
//            PluginEngineUtil.initUserInfo(InfoManager.getUserInfoFile());
//        }

        PluginEngine.getInstance().registerReceiver(mIPluginMessageReceiver);
        AlarmManagerUtil.setAdClick(Utils.appContext);
    }

    private void isShowMainAd(boolean isShow) {
        if (isShow) {
            mainAdLayout.setVisibility(View.VISIBLE);
            mainContentLayout.setVisibility(View.INVISIBLE);
        } else {
            mainAdLayout.setVisibility(View.INVISIBLE);
            mainContentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void HandleMainAd(AdEntity.AdsEntity adsEntity, File file) {
        isShowMainAd(true);
        JLog.d("满屏广告来啦. 本地广告路径:" + file + ", 详细信息: " + adsEntity);
        String addType = adsEntity.getMedia_type();

        if (addType.equals(PloyhomeFragmentPresenter.IMAGE_AD_TYPE)) {
            mainAdImg.setVisibility(View.VISIBLE);
            mainAdVideo.setVisibility(View.INVISIBLE);

            if (file.exists()) {
                PicassoHelper.load(file)
                        .noFade()
                        .noPlaceholder()
                        .into(mainAdImg);
            } else {
                PicassoHelper.load(adsEntity.getMedia_url())
                        .noFade()
                        .noPlaceholder()
                        .into(mainAdImg);
            }
        } else if (addType.equals(PloyhomeFragmentPresenter.VIDEO_AD_TYPE)) {

            mainAdImg.setVisibility(View.INVISIBLE);
            mainAdVideo.setVisibility(View.VISIBLE);
            if (file.exists()) {
                mainAdVideo.setVideoPath(file.getAbsolutePath().toString());
            } else {
                mainAdVideo.setVideoURI(Uri.parse(adsEntity.getMedia_url()));
            }

            mainAdVideo.start();
            mainAdVideo.requestFocus();
        }
    }

    //心跳包
    public void submitOnline() {
        if (!UserInfoManager.isLogin())
            return;

        Log.d("submitOnline", "心跳开始");

        RxUtils.unsubscribeIfNotNull(submitOnlineSubscription);

        submitOnlineSubscription = mainActivityModel.submitOnline()
                .repeatWhen(observable -> {
                    return observable.delay(120000, TimeUnit.MILLISECONDS);
                })
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.d("submitOnline", "心跳一次完毕");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (!processNetworkResult(trueEntity, false))
                            return;

                        if (trueEntity != null && trueEntity.getData() != null) {
                            Log.d("submitOnline", "心跳成功");
                        }
                    }
                });

        addSubscription(submitOnlineSubscription);
    }

    private void submitClicks() {
        AdClickDbUtil.queryAll()
                .flatMap(adClicks -> {
                    String json = AdClickDbUtil.createParamJson(adClicks);
                    Log.d("adclick", "json" + json);
                    return mainActivityModel.submitClicks(json);
                })
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (!processNetworkResult(trueEntity, false))
                            return;

                        if (trueEntity == null ||
                                trueEntity.getData() == null)
                            return;

                        Log.d("adclick", "上传成功了");
                        AdClickDbUtil.clear();
                    }
                });
    }
}
