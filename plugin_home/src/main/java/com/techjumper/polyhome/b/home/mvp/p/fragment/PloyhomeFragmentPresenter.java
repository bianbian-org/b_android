package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.UserInfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.entity.event.AdControllerEvent;
import com.techjumper.commonres.entity.event.AdEvent;
import com.techjumper.commonres.entity.event.AdMainEvent;
import com.techjumper.commonres.entity.event.AdShowEvent;
import com.techjumper.commonres.entity.event.AdTemEvent;
import com.techjumper.commonres.entity.event.HeartbeatTimeEvent;
import com.techjumper.commonres.entity.event.InfoMediaPlayerEvent;
import com.techjumper.commonres.entity.event.NoticeEvent;
import com.techjumper.commonres.entity.event.ShowMainAdEvent;
import com.techjumper.commonres.entity.event.TimerEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.WeatherEvent;
import com.techjumper.commonres.entity.event.pushevent.NoticePushEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.commonres.util.RxUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.adapter.AdViewPagerAdapter;
import com.techjumper.polyhome.b.home.db.util.AdClickDbUtil;
import com.techjumper.polyhome.b.home.mvp.m.PloyhomeFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdNewActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.JujiaActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.ShoppingActivity;
import com.techjumper.polyhome.b.home.mvp.v.fragment.PloyhomeFragment;
import com.techjumper.polyhome.b.home.net.NetHelper;
import com.techjumper.polyhome.b.home.tool.AlarmManagerUtil;
import com.techjumper.polyhome.b.home.utils.DateUtil;
import com.techjumper.polyhome.b.home.widget.AdViewPager;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome.b.home.widget.SquareView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.manager.AdController;
import com.techjumper.polyhome_b.adlib.window.AdWindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/28.
 */
public class PloyhomeFragmentPresenter extends AppBaseFragmentPresenter<PloyhomeFragment> implements AdController.IAlarm, ViewPager.OnPageChangeListener {
    public static final String IMAGE_AD_TYPE = "1";
    public static final String VIDEO_AD_TYPE = "2";

    private PloyhomeFragmentModel model = new PloyhomeFragmentModel(this);
    private int type = -1;
    private UserInfoEntity userInfoEntity;
    private AdController adController;
    private AdEntity.AdsEntity mAdsEntity = new AdEntity.AdsEntity();
    private String addType = IMAGE_AD_TYPE;
    private int videoPosition = 0;
    private ImageView adImageView;
    private MyTextureView textureView;
    //    private boolean mShouldSleep = true;
    private boolean mIsVisibleToUser;
    private boolean mIsGetAd;
    private boolean mIsGetNewAd;
    private float x1, x2, y1, y2;

    private Timer timer = new Timer();
    private int position = 0;
    private List<NoticeEntity.Unread> unreads = new ArrayList<>();
    private List<NoticeEntity.Message> messages = new ArrayList<>();
    private boolean isTimer = true;
    private boolean isOnResume;
    private long heartbeatTime;
    private AdViewPager adViewPager;
    private AdViewPagerAdapter adapter;
    private List<View> views = new ArrayList<>();
    private List<Integer> resIds = new ArrayList<>();
    private int currentPage = 0;
    private LayoutInflater inflater;
    private View currentView;

    private List<AdEntity.AdsEntity> adsEntities = new ArrayList<>();

    @Override
    public void onDestroy() {
        if (adController != null) {
            adController.stopReceiveScreenOff();
            adController.clearPolling();
            adController.stopWakeUpTimer();
            adController.cancelAll();
        }
        cancelTimer();
        super.onDestroy();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        adController = AdController.getInstance();
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getView().getContext());
//
        adImageView = getView().getAd();
        textureView = getView().getTextureView();
        adViewPager = getView().getAdvp();

        adViewPager.setAdapter(adapter = new AdViewPagerAdapter());
        adViewPager.setOffscreenPageLimit(3);
        adViewPager.addOnPageChangeListener(this);
        adViewPager.setCurrentItem(0);

        getAd(true);
        getNotices();

        addSubscription(RxView.clicks(getView().getProperty())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    try {
                        Intent it = new Intent();
                        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.TalkingActivity");
                        it.setComponent(componentName);
                        it.putExtra("com.dnake.talk", "CallingActivity");
                        getView().startActivity(it);
                    } catch (Exception e) {
                        ToastUtils.show("无法打开对讲");
                    }
                }));

//        addSubscription(RxView.clicks(getView().getProperty())
//                .filter(aVoid -> {
//                    if (UserInfoManager.isLogin())
//                        return true;
//
//                    ToastUtils.show(getView().getString(R.string.error_no_login));
//                    return false;
//                })
//                .compose(RxUtil.applySchedulers())
//                .subscribe(aVoid -> {
//                    long familyId = UserInfoManager.getLongFamilyId();
//                    long userId = UserInfoManager.getLongUserId();
//                    String ticket = UserInfoManager.getTicket();
//                    PluginEngineUtil.startProperty(familyId, userId, ticket);
//                }));

        addSubscription(RxView.clicks(getView().getNoticeLayout())
                .filter(aVoid -> {
                    if (UserInfoManager.isLogin())
                        return true;

                    ToastUtils.show(
                            getView().getString(R.string.error_no_login));
                    return false;
                })
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    long userId = UserInfoManager.getLongUserId();
                    long familyId = UserInfoManager.getLongFamilyId();
                    String ticket = UserInfoManager.getTicket();

                    NoticeEntity.InfoUnread infoUnread = new NoticeEntity.InfoUnread();
                    infoUnread.setUnreads(unreads);
                    String unreadString = GsonUtils.toJson(infoUnread);

                    PluginEngineUtil.startInfo(userId, familyId, ticket, type, unreadString);
                }));

//        addSubscription(RxView.clicks(getView().getAd_layout())
//                .filter(aVoid -> {
//                    if (mAdsEntity != null && !TextUtils.isEmpty(mAdsEntity.getMedia_type()))
//                        return true;
//
//                    return false;
//                })
//                .compose(RxUtil.applySchedulers())
//                .subscribe(aVoid -> {
//                    AdClickDbUtil.insert(Long.valueOf(mAdsEntity.getId()), AdController.TYPE_HOME, heartbeatTime);
//                    Intent intent = new Intent(getView().getActivity(), AdActivity.class);
//                    intent.putExtra(AdActivity.ADITEM, mAdsEntity);
//                    getView().getActivity().startActivity(intent);
//                }));

//        addSubscription(RxView.clicks(adViewPager)
//                .compose(RxUtil.applySchedulers())
//                .subscribe(aVoid -> {
//                    AdClickDbUtil.insert(Long.valueOf(mAdsEntity.getId()), AdController.TYPE_HOME, heartbeatTime);
//                    Intent intent = new Intent(getView().getActivity(), AdNewActivity.class);
//                    intent.putExtra(AdActivity.ADITEM, mAdsEntity);
//                    getView().getActivity().startActivity(intent);
//                }));

        addSubscription(RxView.clicks(getView().getShopping())
                .filter(aVoid -> {
                    if (UserInfoManager.isLogin())
                        return true;

                    ToastUtils.show(getView().getString(R.string.error_no_login));
                    return false;
                })
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    Intent intent = new Intent(getView().getActivity(), ShoppingActivity.class);
                    intent.putExtra(ShoppingActivity.TIME, heartbeatTime);
                    getView().startActivity(intent);
                }));

        addSubscription(RxView.clicks(getView().getJujia())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    Intent intent = new Intent(getView().getActivity(), JujiaActivity.class);
                    intent.putExtra(JujiaActivity.TIME, heartbeatTime);
                    getView().startActivity(intent);
                }));

//        getView().getJujia().setClickable(false);

        addSubscription(RxView.clicks(getView().getSmarthome())
                .compose(RxUtil.applySchedulers())
                .subscribe(aVoid -> {
                    PluginEngineUtil.startSmartHome();
                }));

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof NoticeEvent) {
                        NoticeEvent entity = (NoticeEvent) o;
                        if (getView() == null || getView().getNoticeContent() == null || getView().getNoticeTitle() == null)
                            return;

                        if (entity == null || entity.getContent() == null || entity.getTitle() == null)
                            return;

                        Log.d("pluginUserInfo", "更新公告结果: Content: " + entity.getContent() + "Title :" + entity.getTitle());

//                        getView().getNoticeContent().setText(StringUtil.delHTMLTag(entity.getContent()));
                        getView().getNoticeContent().setText(Html.fromHtml(entity.getContent()));
                        getView().getNoticeTitle().setText(TextUtils.isEmpty(entity.getTitle()) ? "" : entity.getTitle());

                        type = entity.getType();
                    } else if (o instanceof UserInfoEvent) {
                        Log.d("pluginUserInfo", "更新公告");
                        getNotices();
                        Log.d("pluginUserInfo", "更新广告");
                        getAd(false);
                        mIsGetNewAd = false;
                    } else if (o instanceof NoticePushEvent) {
                        Log.d("pluginUserInfo", "推送更新公告");
                        getNotices();
                    } else if (o instanceof AdEvent) {
                        Log.d("pluginUserInfo", "推送更新广告");
                        getAd(false);
                    }
                }));

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof WeatherEvent) {
                        WeatherEvent event = (WeatherEvent) o;
                        WeatherEntity.WeatherDataEntity entity = event.getWeatherEntity();

                        SquareView restrictSv = getView().getFpRestrict();
                        SquareView temperatureSv = getView().getFpTemperature();

                        temperatureSv.showContentText(TextUtils.isEmpty(entity.getPm25()) ? "0" : entity.getPm25());
                        temperatureSv.showTitleText("pm2.5");

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
//                            mShouldSleep = false;
                            adController.interrupt(AdController.TYPE_WAKEUP);
                            adController.interrupt(AdController.TYPE_SLEEP);
                        }
                    } else if (o instanceof ShowMainAdEvent) {
                        if (mIsGetAd) {
                            getNormalAd(true);
                        }
                    } else if (o instanceof InfoMediaPlayerEvent) {
                        InfoMediaPlayerEvent event = (InfoMediaPlayerEvent) o;
                        if (event.getType() == InfoMediaPlayerEvent.PLOY) {
                            if (textureView != null) {
                                Log.d("ergou", "initMediaPlayer");
                                textureView.initMediaPlayer();
                            }
                        }
                    } else if (o instanceof TimerEvent) {
                        TimerEvent event = (TimerEvent) o;
                        isTimer = event.isTimer();
                        if (isTimer) {
                            if (isOnResume &&
                                    messages != null &&
                                    messages.size() > 0) {
                                initTimer();
                            }
                        } else {
                            cancelTimer();
                        }
                    } else if (o instanceof HeartbeatTimeEvent) {
                        HeartbeatTimeEvent event = (HeartbeatTimeEvent) o;
                        heartbeatTime = event.getTime();
                    }
                }));

        AlarmManagerUtil.setNoticeTime(Utils.appContext);

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
                            intent.putExtra(AdNewActivity.TYPE, AdNewActivity.TYPE_ONE);
                            intent.putExtra(AdNewActivity.TIME, heartbeatTime);
                            getView().getActivity().startActivity(intent);
                        }
                        adController.startAdTimer(AdController.TYPE_HOME, adViewPager.getCurrentItem());
                        break;
                }
                return false;
            }
        });

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
                        if (noticeEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                            getAgainNotices();
                            return;
                        }

                        if (!processNetworkResult(noticeEntity, false))
                            return;

                        if (noticeEntity == null ||
                                noticeEntity.getData() == null)
                            return;

                        initNotices(noticeEntity.getData());
                    }
                }));
    }

    public void getAgainNotices() {
        if (!UserInfoManager.isLogin())
            return;

        addSubscription(model.submitOnline()
                .flatMap(heartbeatEntity -> {
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    return model.getNotices();
                }).subscribe(new Subscriber<NoticeEntity>() {
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

                        initNotices(noticeEntity.getData());
                    }
                }));
    }

    public void initNotices(NoticeEntity.NoticeDataEntity entity) {
        if (entity == null)
            return;

        if (timer != null) {
            timer.cancel();
        }

        if (unreads != null && unreads.size() > 0)
            unreads.clear();

        unreads = entity.getUnread();
        if (unreads != null) {
            int num = 0;
            if (unreads.size() > 0) {
                for (int i = 0; i < unreads.size(); i++) {
                    num += unreads.get(i).getCount();
                }
            }
            getView().getNoticeNum().setText(String.valueOf(num));
        }

        messages = entity.getMessages();
        position = 0;

        if (messages != null && messages.size() > 0) {
            if (messages.size() == 1) {
                NoticeEntity.Message message = messages.get(0);
                RxBus.INSTANCE.send(new NoticeEvent(message.getTitle(), message.getContent(), message.getTypes()));
            } else {
                initTimer();
            }
        } else {
            RxBus.INSTANCE.send(new NoticeEvent(getView().getString(R.string.info_not_new), "", -1));
        }
    }

    private void initTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NoticeEntity.Message message = messages.get(position);

                RxBus.INSTANCE.send(new NoticeEvent(message.getTitle(), message.getContent(), message.getTypes()));

                if (position == messages.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
            }
        }, 1000, 3000);
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnResume = false;
        if (mIsVisibleToUser) {
            if (adController != null) {
                adController.cancel(AdController.TYPE_HOME);
            }
            initAd();
            mIsGetAd = false;
            RxBus.INSTANCE.send(new TimerEvent(false));
        }
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        adViewPager = getView().getAdvp();
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnResume = true;
        Log.d("ergou", "onResume");
        if (mIsVisibleToUser) {
            if (textureView != null) {
                Log.d("ergou", "initMediaPlayer");
                textureView.initMediaPlayer();
            }
            RxBus.INSTANCE.send(new InfoMediaPlayerEvent(InfoMediaPlayerEvent.INFO));
            RxBus.INSTANCE.send(new TimerEvent(true));
            getNormalAd(mIsGetNewAd);
            mIsGetAd = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            Log.d("hehe", "Ployhome显示");
            getNormalAd(mIsGetNewAd);
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
            restrictNo = restrict.getSunday();
        } else if (date.equals("周二")) {
            restrictNo = restrict.getMonday();
        } else if (date.equals("周三")) {
            restrictNo = restrict.getTuesday();
        } else if (date.equals("周四")) {
            restrictNo = restrict.getWednesday();
        } else if (date.equals("周五")) {
            restrictNo = restrict.getThursday();
        } else if (date.equals("周六")) {
            restrictNo = restrict.getFriday();
        } else if (date.equals("周日")) {
            restrictNo = restrict.getSaturday();
        }

        return restrictNo;
    }

    private void getAd(boolean fromCahce) {
        if (!UserInfoManager.isLogin())
            return;

        JLog.d("广告请求");

        if (mIsGetAd) {
            getNormalAd(fromCahce);
        }

        Log.d("mIsGetNewAd", "fromCahce " + fromCahce + "");
        getWakeUpAd(fromCahce);
        getSleepAd(fromCahce);

        RxBus.INSTANCE.send(new AdTemEvent(fromCahce));
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
        if (file == null)
            return;
        addType = adsEntity.getMedia_type();

        if (adImageView == null || textureView == null)
            return;
        if (IMAGE_AD_TYPE.equals(addType)) {
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
        } else if (VIDEO_AD_TYPE.equals(addType)) {
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

    /**
     * 获取普通广告
     *
     * @param fromCahce
     */
    private void getNormalAd(boolean fromCahce) {
        Log.d("isShow", AdWindowManager.getInstance().isShow() + "");
        if (AdWindowManager.getInstance().isShow())
            return;

        if (!UserInfoManager.isLogin() || adController == null)
            return;

        Log.d("hehe", "ployhome普通获取广告");
        adController.startPolling(this, fromCahce);
    }

    /**
     * 唤醒广告
     *
     * @param fromCahce
     */
    private void getWakeUpAd(boolean fromCahce) {
        if (!UserInfoManager.isLogin() || adController == null)
            return;

        adController.startWakeUpTimer(() -> {

            if (adController.isScreenOn()) {
                JLog.d("屏幕已经是点亮状态, 不再做操作");
                return;
            } else if (adController.isRunning(AdController.TYPE_WAKEUP)
                    || adController.isRunning(AdController.TYPE_SLEEP)) {
                JLog.d("已经在执行唤醒或者休眠广告,不再重复唤醒");
                return;
            }
            JLog.d("获取唤醒广告" + UserInfoManager.getFamilyId() + "  " + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket());
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
            adController.executeAdRule(AdController.TYPE_WAKEUP, UserInfoManager.getFamilyId()
                    , UserInfoManager.getUserId(), UserInfoManager.getTicket()
                    , fromCahce
                    , new AdController.IExecuteRule() {

                        @Override
                        public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                            adController.wakeUpScreen();
                            RxBus.INSTANCE.send(new AdMainEvent(adsEntity, file));
                        }

                        @Override
                        public void onAdPlayFinished() {
                            JLog.d("广告播放完成  ");
                            adController.turnOffScreen(); //保持休眠
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
     *
     * @param fromCahce
     */
    private void getSleepAd(boolean fromCahce) {
        if (!UserInfoManager.isLogin() || adController == null)
            return;

        adController.receiveScreenOff(() -> {
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
            if (adController.isRunning(AdController.TYPE_WAKEUP)
                    || adController.isRunning(AdController.TYPE_SLEEP)) {
                JLog.d("已经在执行唤醒或者休眠广告,不再重复执行休眠广告");
                return;
            }
            JLog.d("获取休眠广告" + UserInfoManager.getFamilyId() + "  " + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket());
            adController.executeAdRule(AdController.TYPE_SLEEP, UserInfoManager.getFamilyId()
                    , UserInfoManager.getUserId(), UserInfoManager.getTicket()
                    , fromCahce
                    , new AdController.IExecuteRule() {
                        @Override
                        public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                            adController.wakeUpScreen();  //唤醒屏幕
                            RxBus.INSTANCE.send(new AdMainEvent(adsEntity, file));
                        }

                        @Override
                        public void onAdPlayFinished() {
                            JLog.d("广告播放完成  ");
//                            if (!mShouldSleep) {
//                                mShouldSleep = true;
//                            } else {
                            adController.turnOffScreen(); //保持休眠
//                            }
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

    @Override
    public void onAlarmReceive(boolean fromCache) {
        JLog.d("普通获取广告" + UserInfoManager.getFamilyId() + "  " + UserInfoManager.getUserId() + "  " + UserInfoManager.getTicket());
//                adController.executeAdRule(AdController.TYPE_HOME, "434", "362", "5b279ba4e46853d86e1d109914cfebe3ca224381", new AdController.IExecuteRule() {
        adController.executeAdRule(AdController.TYPE_HOME, UserInfoManager.getFamilyId()
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
                                if (IMAGE_AD_TYPE.equals(addType)) {
                                    ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_ad_image, null);

                                    views.add(imageView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                } else if (VIDEO_AD_TYPE.equals(addType)) {

                                    MyTextureView textureView = (MyTextureView) inflater.inflate(R.layout.layout_ad_video, null);

                                    views.add(textureView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                }
                            }
                        }
                        adapter.setViews(views, adsEntities);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                        Log.d("adsEntity", "adsEntity: " + adsEntity);
                        Log.d("adsEntity", "file: " + file);
                        Log.d("adsEntity", "file.getAbsolutePath(): " + file.getAbsolutePath());
//                        HandleAd(adsEntity, file);
                        Log.d("ad12", "跳下一页, 当前页" + currentPage);

                        if (adViewPager != null) {
                            adViewPager.setCurrentItem(currentPage, false);
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
            if (mAdsEntity.getMedia_type().equals(VIDEO_AD_TYPE)) {
                if (currentView != null) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
                currentView = adViewPager.findViewWithTag(position);

                if (currentView == null)
                    return;

                adapter.playVideo(currentView, mAdsEntity.getFile());
            } else {
                if (currentView != null) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2 && mAdsEntity != null) {
            AdClickDbUtil.insert(Long.valueOf(mAdsEntity.getId()), AdController.TYPE_HOME, ComConstant.AD_TYPE_SLIDE, heartbeatTime);
        }
    }
}
