package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.entity.event.AdControllerEvent;
import com.techjumper.commonres.entity.event.AdEvent;
import com.techjumper.commonres.entity.event.AdMainEvent;
import com.techjumper.commonres.entity.event.AdShowEvent;
import com.techjumper.commonres.entity.event.AdTemEvent;
import com.techjumper.commonres.entity.event.NoticeEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.WeatherEvent;
import com.techjumper.commonres.entity.event.pushevent.NoticePushEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.m.PloyhomeFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.JujiaActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.ShoppingActivity;
import com.techjumper.polyhome.b.home.mvp.v.fragment.PloyhomeFragment;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;
import com.techjumper.polyhome.b.home.utils.DateUtil;
import com.techjumper.polyhome.b.home.utils.StringUtil;
import com.techjumper.polyhome.b.home.widget.MyVideoView;
import com.techjumper.polyhome.b.home.widget.SquareView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.manager.AdController;

import java.io.File;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/28.
 */
public class PloyhomeFragmentPresenter extends AppBaseFragmentPresenter<PloyhomeFragment> {
    public static final String IMAGE_AD_TYPE = "1";
    public static final String VIDEO_AD_TYPE = "2";

    private PloyhomeFragmentModel model = new PloyhomeFragmentModel(this);
    private int type = -1;
    private UserInfoEntity userInfoEntity;
    private AdController adController;
    private AdEntity.AdsEntity mAdsEntity = new AdEntity.AdsEntity();
    private String addType = IMAGE_AD_TYPE;
    private int videoPosition = 0;
    private MyVideoView video;
    private ImageView adImageView;
    private boolean mShouldSleep = true;
    private boolean mIsVisibleToUser;
    private boolean mIsGetAd;

    @OnClick(R.id.property)
    void property() {
        if (UserInfoManager.isLogin()) {
            long familyId = UserInfoManager.getLongFamilyId();
            long userId = UserInfoManager.getLongUserId();
            String ticket = UserInfoManager.getTicket();
            PluginEngineUtil.startProperty(familyId, userId, ticket);
        } else {
            ToastUtils.show(getView().getString(R.string.error_no_login));
        }
    }

    @OnClick(R.id.notice_layout)
    void noticeLayout() {
        if (UserInfoManager.isLogin()) {
            long userId = UserInfoManager.getLongUserId();
            long familyId = UserInfoManager.getLongFamilyId();
            String ticket = UserInfoManager.getTicket();

            NoticeEntity.InfoUnread infoUnread = new NoticeEntity.InfoUnread();
            infoUnread.setUnreads(getView().getUnreads());
            String unreadString = GsonUtils.toJson(infoUnread);

            PluginEngineUtil.startInfo(userId, familyId, ticket, type, unreadString);
        } else {
            ToastUtils.show(getView().getString(R.string.error_no_login));
        }
    }

    @OnClick(R.id.ad)
    void ad() {
        if (mAdsEntity == null || TextUtils.isEmpty(mAdsEntity.getMedia_type()))
            return;

        Intent intent = new Intent(getView().getActivity(), AdActivity.class);
        intent.putExtra(AdActivity.ADITEM, mAdsEntity);
        getView().getActivity().startActivity(intent);
    }

    @OnClick(R.id.shopping)
    void shopping() {
        if (TextUtils.isEmpty(UserInfoManager.getTicket()) || TextUtils.isEmpty(UserInfoManager.getUserId())) {
            ToastUtils.show(getView().getString(R.string.error_no_login));
        } else {
            Intent intent = new Intent(getView().getActivity(), ShoppingActivity.class);
            getView().startActivity(intent);
        }
    }

    @OnClick(R.id.jujia)
    void jujia() {
        Intent intent = new Intent(getView().getActivity(), JujiaActivity.class);
        getView().startActivity(intent);
    }

    @OnClick(R.id.smarthome)
    void smartHome() {
        PluginEngineUtil.startSmartHome();
    }

    @Override
    public void onDestroy() {
        if (adController != null) {
            adController.stopReceiveScreenOff();
            adController.clearPolling();
            adController.stopWakeUpTimer();
            adController.cancelAll();
        }
        super.onDestroy();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        adController = AdController.getInstance();
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        adImageView = getView().getAd();
        video = getView().getVideo();

        getAd();
        getNotices();

        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof NoticeEvent) {
                        NoticeEvent entity = (NoticeEvent) o;
                        if (getView() == null || getView().getNoticeContent() == null || getView().getNoticeTitle() == null)
                            return;

                        if (entity == null || entity.getContent() == null || entity.getTitle() == null)
                            return;

                        Log.d("pluginUserInfo", "更新公告结果: Content: " + entity.getContent() + "Title :" + entity.getTitle());

                        getView().getNoticeContent().setText(StringUtil.delHTMLTag(entity.getContent()));
                        getView().getNoticeTitle().setText(TextUtils.isEmpty(entity.getTitle()) ? "" : entity.getTitle());

                        type = entity.getType();
                    } else if (o instanceof UserInfoEvent) {
                        Log.d("pluginUserInfo", "更新公告");
                        getNotices();
                        Log.d("pluginUserInfo", "更新广告");
                        getAd();
                    } else if (o instanceof NoticePushEvent) {
                        Log.d("pluginUserInfo", "推送更新公告");
                        getNotices();
                    } else if (o instanceof AdEvent) {
                        Log.d("pluginUserInfo", "推送更新广告");
                        getAd();
                    }
                });

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof WeatherEvent) {
                        WeatherEvent event = (WeatherEvent) o;
                        WeatherEntity.WeatherDataEntity entity = event.getWeatherEntity();

                        SquareView restrictSv = getView().getFpRestrict();
                        SquareView temperatureSv = getView().getFpTemperature();

                        temperatureSv.showContentText(TextUtils.isEmpty(entity.getTemperature()) ? "0" : entity.getTemperature() + "°");
                        temperatureSv.showTitleText("pm2.5 " + (TextUtils.isEmpty(entity.getPm25()) ? "0" : entity.getPm25()));

                        String date = entity.getDate_one();
                        String restrictNo = getRestrictNo(date, entity.getRestrict());

                        if (TextUtils.isEmpty(restrictNo)) {
                            restrictSv.showContentText(DateUtil.getDate(date));
                            restrictSv.showContentTextSize(40);
                            restrictSv.showTitleText("不限行");
                        } else {
                            restrictSv.showContentText(restrictNo);
                            restrictSv.showContentTextSize(60);
                            restrictSv.showTitleText(DateUtil.getDate(date) + "限行");
                        }
                    } else if (o instanceof AdControllerEvent) {
                        if (adController != null) {
                            mShouldSleep = false;
                            adController.cancel(AdController.TYPE_WAKEUP);
                            adController.cancel(AdController.TYPE_SLEEP);
                        }
                    }
                }));

        AlarmManagerUtil.setNoticeTime(Utils.appContext);
    }

    private void getNotices() {
        if (!UserInfoManager.isLogin())
            return;

        addSubscription(model.getNotices()
                .subscribe(new Subscriber<NoticeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(NoticeEntity noticeEntity) {
                        if (!processNetworkResult(noticeEntity, false))
                            return;

                        if (noticeEntity == null ||
                                noticeEntity.getData() == null)
                            return;

                        getView().initNotices(noticeEntity.getData());
                    }
                }));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsVisibleToUser) {
            if (adController != null) {
                adController.cancel(AdController.TYPE_HOME);
            }
            initAd();
            mIsGetAd = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsVisibleToUser) {
            getNormalAd();
            mIsGetAd = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            Log.d("hehe", "Ployhome显示");
            getNormalAd();
            mIsGetAd = true;
        } else {
            Log.d("hehe", "Ployhome消失");
            if (adController != null) {
                adController.cancel(AdController.TYPE_HOME);
            }
            initAd();
            mIsGetAd = false;
        }
    }

    private String getRestrictNo(String date, WeatherEntity.Restrict restrict) {
        String restrictNo = "";

        if (TextUtils.isEmpty(date))
            return "";

        if (date.equals("周一")) {
            restrictNo = restrict.getMonday();
        } else if (date.equals("周二")) {
            restrictNo = restrict.getTuesday();
        } else if (date.equals("周三")) {
            restrictNo = restrict.getWednesday();
        } else if (date.equals("周四")) {
            restrictNo = restrict.getThursday();
        } else if (date.equals("周五")) {
            restrictNo = restrict.getFriday();
        } else if (date.equals("周六")) {
            restrictNo = restrict.getSaturday();
        } else if (date.equals("周日")) {
            restrictNo = restrict.getSunday();
        }

        return restrictNo;
    }

    private void getAd() {
        if (!UserInfoManager.isLogin())
            return;

        JLog.d("广告请求");

        if (mIsVisibleToUser) {
            getNormalAd();
        }
        getWakeUpAd();
        getSleepAd();

        RxBus.INSTANCE.send(new AdTemEvent());
    }

    /**
     * 初始化广告
     */
    private void initAd() {
        if (adImageView == null || video == null)
            return;

        video.pause();
        adImageView.setVisibility(View.VISIBLE);
        video.setVisibility(View.INVISIBLE);
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

        if (addType.equals(IMAGE_AD_TYPE)) {
            adImageView.setVisibility(View.VISIBLE);
            video.setVisibility(View.INVISIBLE);
//            video.setZOrderOnTop(true);

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
        } else if (addType.equals(VIDEO_AD_TYPE)) {

            adImageView.setVisibility(View.INVISIBLE);
            video.setVisibility(View.VISIBLE);
//            video.setZOrderOnTop(true);
            if (file.exists()) {
                video.setVideoURI(Uri.parse(file.getAbsolutePath()));
            } else {
                video.setVideoURI(Uri.parse(adsEntity.getMedia_url()));
            }

            video.setZOrderOnTop(true);
            video.start();
            video.requestFocus();

            adsEntity.setMedia_url(file.getAbsolutePath());
        }

        mAdsEntity = adsEntity;
    }

    /**
     * 获取普通广告
     */
    private void getNormalAd() {
        if (!UserInfoManager.isLogin() || adController == null)
            return;

        adController.startPolling(() -> {
            JLog.d("普通获取广告" + UserInfoManager.getFamilyId() + "  " + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket());
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
            adController.executeAdRule(AdController.TYPE_HOME, UserInfoManager.getFamilyId(), UserInfoManager.getUserId(), UserInfoManager.getTicket(), new AdController.IExecuteRule() {
                @Override
                public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                    Log.d("adsEntity", "adsEntity: " + adsEntity);
                    Log.d("adsEntity", "file: " + file);
                    Log.d("adsEntity", "file.getAbsolutePath(): " + file.getAbsolutePath());
                    HandleAd(adsEntity, file);
                }

                @Override
                public void onAdPlayFinished() {
                    JLog.d("广告播放完成  ");
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
        });
    }

    /**
     * 唤醒广告
     */
    private void getWakeUpAd() {
        if (!UserInfoManager.isLogin() || adController == null)
            return;

        adController.startWakeUpTimer(() -> {
            JLog.d("获取唤醒广告" + UserInfoManager.getFamilyId() + "  " + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket());
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
            adController.executeAdRule(AdController.TYPE_WAKEUP, UserInfoManager.getFamilyId(), UserInfoManager.getUserId(), UserInfoManager.getTicket(), new AdController.IExecuteRule() {
                boolean mIsWakedUp; //是否被唤醒了

                @Override
                public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                    boolean wakedUp = adController.wakeUpScreen();
                    if (!mIsWakedUp && !wakedUp) {
                        adController.interrupt(AdController.TYPE_WAKEUP);
                        return;
                    }
                    mIsWakedUp = true;
                    RxBus.INSTANCE.send(new AdMainEvent(adsEntity, file));
                }

                @Override
                public void onAdPlayFinished() {
                    JLog.d("广告播放完成  ");
                    adController.turnOffScreen(); //保持休眠
                    mIsWakedUp = false;
                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }

                @Override
                public void onAdDownloadError(AdEntity.AdsEntity adsEntity) {
                    JLog.d("某个广告下载失败: " + adsEntity);
                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }

                @Override
                public void onAdExecuteFailed(String reason) {
                    JLog.d("获取广告失败: " + reason);
                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }

                @Override
                public void onAdNoExist(String adType, String hour) {
                    JLog.d("没有广告: 广告类型=" + adType + ", 当前小时=" + hour);
                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }
            });
        });
    }

    /**
     * 休眠广告
     */
    private void getSleepAd() {
        if (!UserInfoManager.isLogin() || adController == null)
            return;

        adController.receiveScreenOff(() -> {
            JLog.d("获取休眠广告" + UserInfoManager.getFamilyId() + "  " + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket());
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
            adController.executeAdRule(AdController.TYPE_SLEEP, UserInfoManager.getFamilyId(), UserInfoManager.getUserId(), UserInfoManager.getTicket(), new AdController.IExecuteRule() {
                @Override
                public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                    adController.wakeUpScreen();  //唤醒屏幕
                    RxBus.INSTANCE.send(new AdMainEvent(adsEntity, file));
                }

                @Override
                public void onAdPlayFinished() {
                    JLog.d("广告播放完成  ");
                    if (!mShouldSleep) {
                        adController.resetSleepTime();
                        mShouldSleep = true;
                    } else {
                        adController.turnOffScreen(); //保持休眠
                    }
                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }

                @Override
                public void onAdDownloadError(AdEntity.AdsEntity adsEntity) {
                    JLog.d("某个广告下载失败: " + adsEntity);
                    adController.turnOffScreen(); //保持休眠
                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }

                @Override
                public void onAdExecuteFailed(String reason) {
                    JLog.d("获取广告失败: " + reason);

                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }

                @Override
                public void onAdNoExist(String adType, String hour) {
                    JLog.d("没有广告: 广告类型=" + adType + ", 当前小时=" + hour);
                    adController.turnOffScreen(); //保持休眠
                    RxBus.INSTANCE.send(new AdShowEvent(false));
                }
            });
        });
    }
}
