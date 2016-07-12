package com.techjumper.polyhome_b.adlib.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.system.PowerUtil;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome_b.adlib.R;
import com.techjumper.polyhome_b.adlib.download.AdDownloadManager;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.net.RetrofitTemplate;
import com.techjumper.polyhome_b.adlib.services.AlarmService;
import com.techjumper.polyhome_b.adlib.services.WakeupAdService;
import com.techjumper.polyhome_b.adlib.utils.PollingUtils;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdController {

    private static AdController INSTANCE;

    public static final int CODE_WAKEUP_ALARM = 99; //唤醒广告服务的requestCode;
    public static final int CODE_ALARM_SERVICE = 100;

    public static final String TYPE_HOME = "1001"; //'室内机 - 首页'
    public static final String TYPE_VIDEO = "1002";  //室内机 - 视频通话
    public static final String TYPE_SLEEP = "1003"; //室内机 - 休眠
    public static final String TYPE_WAKEUP = "1004"; //室内机 - 唤醒

    public static final String AD_DATA_DIR = "ad";
    public static final String AD_DATA_name = "addata";
    public static final String DOWNLOAD_SUB_PATH = "mediacache";
    private static File mAdFile = new File(Utils.appContext.getCacheDir().getAbsolutePath() + File.separator + AD_DATA_DIR);

    private Subscription mPadAdSubs;
    private HashMap<String, AdRuleExecutor> mExecutorMap = new HashMap<>();

    private IWakeUp iWakeUP;
    private ScreenOffReceiver mScreenOffReceiver;
    private Subscription mWakeUpSubs;
    private Subscription mAlarmSubs;

    private int mLockTime;
    private IAlarm iAlarm;

    public AdController() {
        mLockTime = getScreenOffTime();
        if (mLockTime == 0) {
            mLockTime = 40 * 60 * 1000;
            setScreenOffTime(mLockTime);
        }
    }

    public void wakeUpScreen() {
        if (isScreenOn()) {
            JLog.d("屏幕已经是亮的所以不做操作");
            return;
        }
        JLog.d("唤醒屏幕");
        PowerUtil.wakeUpScreen();
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) Utils.appContext.getSystemService(Context.POWER_SERVICE);
        return Build.VERSION.SDK_INT >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();

    }

    public void startWakeUpTimer(IWakeUp iWakeUp) {
        stopWakeUpTimer();
        this.iWakeUP = iWakeUp;
        mWakeUpSubs = RxBus.INSTANCE.asObservable()
                .subscribe(o -> {
                    if (!(o instanceof WakeupAdService.WakeupAdEvent))
                        return;
                    if (this.iWakeUP != null) {
                        this.iWakeUP.onWakeUpAdExecute();
                    }
                });
        //            Calendar c = Calendar.getInstance();
//            long oneHourLater = System.currentTimeMillis() + 1000L * 60 * 60;
        long triggerTime = getTriggerTime();
//            c.setTimeInMillis(oneHourLater);
//            c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//            c.set(Calendar.MINUTE, 0);
//            c.set(Calendar.SECOND, 0);
//            c.set(Calendar.MILLISECOND, 0);

        PollingUtils.startPollingService(Utils.appContext
                , triggerTime, 60 * 60L, WakeupAdService.class, "", CODE_WAKEUP_ALARM);
//        if (this.iWakeUP != null) {
//            this.iWakeUP.onWakeUpAdExecute();
//        }
//        PollingUtils.startPollingService(Utils.appContext
//                , oneHourLater, 5L, WakeupAdService.class, "", CODE_WAKEUP_ALARM);
    }

    public void stopWakeUpTimer() {
        RxUtils.unsubscribeIfNotNull(mWakeUpSubs);
        PollingUtils.stopPollingService(Utils.appContext
                , WakeupAdService.class, "", CODE_WAKEUP_ALARM);
        iWakeUP = null;
    }


    public void startPolling(IAlarm iAlarm) {
        stopPolling();
        this.iAlarm = iAlarm;
        mAlarmSubs = RxBus.INSTANCE.asObservable()
                .subscribe(o -> {
                    if (!(o instanceof AlarmService.AlarmServiceEvent))
                        return;
                    if (this.iAlarm != null) {
                        this.iAlarm.onAlarmReceive();
                    }
                });
        long timeMillis = getTriggerTime();

        PollingUtils.startPollingService(Utils.appContext
                , timeMillis, 60 * 60L, AlarmService.class, "", CODE_ALARM_SERVICE);
        if (this.iAlarm != null) {
            this.iAlarm.onAlarmReceive();
        }
//        PollingUtils.startPollingService(Utils.appContext
//                , timeMillis, 30L, AlarmService.class, "", CODE_ALARM_SERVICE);

    }

    public void stopPolling() {
        RxUtils.unsubscribeIfNotNull(mAlarmSubs);
        PollingUtils.stopPollingService(Utils.appContext
                , AlarmService.class, "", CODE_ALARM_SERVICE);
        iAlarm = null;
    }

    private long getTriggerTime() {
        Calendar c = Calendar.getInstance();
        long oneHourLater = System.currentTimeMillis() + 1000L * 60 * 60;
//        long oneHourLater = System.currentTimeMillis() + 1000L * 5;
        c.setTimeInMillis(oneHourLater);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
//        return oneHourLater;
        return c.getTimeInMillis();
    }

    public void receiveScreenOff(IScreenOff iScreenOff) {
        stopReceiveScreenOff();
        mScreenOffReceiver = new ScreenOffReceiver(iScreenOff, mLockTime);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        Utils.appContext.registerReceiver(mScreenOffReceiver, intentFilter);
        JLog.d("开始接收屏幕关闭的消息");
    }

    public void resetSleepTime() {
        JLog.d("恢复休眠时间: " + mLockTime);
        setScreenOffTime(mLockTime);
    }

    public void stopReceiveScreenOff() {
        try {
            Utils.appContext.unregisterReceiver(mScreenOffReceiver);
        } catch (Exception ignored) {
        }
        JLog.d("停止接收屏幕关闭的消息");
    }

    public void executeAdRule(String adType, String family_id, String user_id, String ticket, IExecuteRule iExecuteRule) {
        if (TextUtils.isEmpty(adType)) return;
        cancel(adType);
        AdRuleExecutor executor = mExecutorMap.get(adType);
        if (executor == null) {
            executor = new AdRuleExecutor(adType);
            mExecutorMap.put(adType, executor);
        }
        executor.fetchAd(family_id, user_id, ticket, iExecuteRule);
    }

    public void cancelAll() {
        Iterator<Map.Entry<String, AdRuleExecutor>> it = mExecutorMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, AdRuleExecutor> next = it.next();
            if (next == null) continue;
            AdRuleExecutor executor = next.getValue();
            if (executor != null) {
                executor.interrupt();
            }
            it.remove();
        }
    }

    public void cancel(String adType) {
        if (adType == null) return;
        AdRuleExecutor executor = mExecutorMap.get(adType);
        if (executor != null) {
            executor.interrupt();
            mExecutorMap.put(adType, null);
        }
    }

    private void fetchAd(String family_id, String user_id, String ticket, IFetchAd iFetchAd) {
        mPadAdSubs = RetrofitTemplate.getInstance().padAd(family_id, user_id, ticket)
                .subscribe(new Subscriber<AdEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String data = getJsonFromLocal();
                        if (iFetchAd != null && TextUtils.isEmpty(data)) {
                            JLog.d("<ad> 获取广告信息失败: " + e);
                            iFetchAd.onAdInfoReceive(null);
                            return;
                        }
                        AdEntity adEntity = GsonUtils.fromJson(data, AdEntity.class);
                        if (iFetchAd != null) {
                            iFetchAd.onAdInfoReceive(adEntity);
                            if (adEntity != null) {
                                JLog.d("<ad> 本地获取广告信息成功: " + adEntity.toString());
                            } else {
                                JLog.d("<ad> 获取广告信息失败: " + e);
                            }
                        }
                    }

                    @Override
                    public void onNext(AdEntity adEntity) {
                        if (adEntity == null) {
                            ToastUtils.show(Utils.appContext.getString(R.string.error_to_connect_server));
                            return;
                        }

                        JLog.d("<ad> 获取广告信息成功: " + adEntity.toString());
//                        if (BuildConfig.DEBUG) {
                        ToastUtils.show("<ad> 获取广告信息成功");
//                        }
                        saveJsonToLocal(GsonUtils.toJson(adEntity));
                        cacheFile(adEntity);

                        if (iFetchAd != null) {
                            iFetchAd.onAdInfoReceive(adEntity);
                        }
                    }
                });
    }


    private void cacheFile(AdEntity adEntity) {
        List<HashMap<String, List<AdEntity.AdsEntity>>> ads = adEntity.getAds();
        if (ads == null) return;
        for (HashMap<String, List<AdEntity.AdsEntity>> nextMap : ads) {
            for (Map.Entry<String, List<AdEntity.AdsEntity>> next : nextMap.entrySet()) {
                List<AdEntity.AdsEntity> value = next.getValue();
                if (value == null)
                    continue;
                for (AdEntity.AdsEntity adsEntity : value) {
                    String media_url = adsEntity.getMedia_url();
                    SystemClock.sleep(20);
                    AdDownloadManager.getInstance().download(adsEntity.getMd5(), media_url, null);
                }

            }
        }
    }


    public void cancelFetchAdFromServer() {
        RxUtils.unsubscribeIfNotNull(mPadAdSubs);
    }

    private String getJsonFromLocal() {
        return FileUtils.loadTextFile(getAdDataPath(), AD_DATA_name);

    }

    private boolean saveJsonToLocal(String json) {
        return FileUtils.saveStringToPath(json, getAdDataPath(), AD_DATA_name);
    }

    private String getAdDataPath() {
        return mAdFile.getAbsolutePath();
    }


    public void turnOffScreen() {
        setScreenOffTime(0);
//        if (mScreenOffReceiver == null) {
//            throw new NullPointerException("please call receiveScreenOff() first");
//        }
//        receiveScreenOff(mScreenOffReceiver.getScreenOffListener());
    }

    private static int getScreenOffTime() {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(Utils.appContext.getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception ignored) {

        }
//        JLog.d("当前系统休眠时间为: " + screenOffTime);
        return screenOffTime;
    }

    private static void setScreenOffTime(int paramInt) {
        try {
            boolean b = Settings.System.putInt(Utils.appContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    paramInt);
            if (!b)
                JLog.e("设置系统休眠时间失败");
        } catch (Exception localException) {
            JLog.e("设置系统休眠时间失败");
        }
    }

    public interface IFetchAd {
        void onAdInfoReceive(AdEntity adData);
    }

    public interface IExecuteRule {

        void onAdReceive(AdEntity.AdsEntity adsEntity, File file);

        void onAdPlayFinished();

        void onAdDownloadError(AdEntity.AdsEntity adsEntity);

        void onAdExecuteFailed(String reason);

        void onAdNoExist(String adType, String hour);
    }

    public interface ITimer {
        void onTimerFinished(boolean success);
    }

    public interface IWakeUp {
        void onWakeUpAdExecute();
    }

    public interface IAlarm {
        void onAlarmReceive();
    }

    public interface IScreenOff {
        void onSleepAdExecute();
    }

    public static class ScreenOffReceiver extends BroadcastReceiver {

        private IScreenOff iScreenOff;
        private int mScreenOffTime;

        public ScreenOffReceiver(IScreenOff iScreenOff, int screenOffTime) {
            this.iScreenOff = iScreenOff;
            mScreenOffTime = screenOffTime;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getScreenOffTime() != mScreenOffTime) {
                setScreenOffTime(mScreenOffTime);
                JLog.d("代码控制关闭屏幕, 不触发回调");
                return;
            }
            JLog.d("屏幕关闭了");
            if (iScreenOff != null) {
                iScreenOff.onSleepAdExecute();
            }
        }

        public IScreenOff getScreenOffListener() {
            return iScreenOff;
        }
    }

    private class AdRuleExecutor {
        public static final String ERROR_CONNECT_SERVER = "无法获取广告数据";
        public static final String ERROR_DOWNLOAD_AD_ERROR = "下载广告失败";
        public static final String ERROR_AD_TYPE_IS_NULL = "广告类型为 null";
        public static final String ERROR_NO_MATCHING_AD_TYPE = "没有匹配的广告类型";
        public static final String ERROR_NO_TIME_LENGTH = "未指定持续时间";
        public static final String ERROR_NO_ADS = "无广告数据";

        private IExecuteRule iExecuteRule;
        private String mRuleType;
        private String mFamilyId;
        private String mUrserId;
        private String mTicket;
        private int mCurrentTimerIndex;
        private long mAlreadyExecuteTime;
        private boolean mInterrupt;


        public AdRuleExecutor(String ruleType) {
            mRuleType = ruleType;
            if (mRuleType == null) {
                mRuleType = "";
            }
        }

        public void fetchAd(String family_id, String user_id, String ticket, IExecuteRule iExecuteRule) {

            this.mFamilyId = family_id;
            this.mUrserId = user_id;
            this.mTicket = ticket;

            this.iExecuteRule = iExecuteRule;
            if (TextUtils.isEmpty(mRuleType)) {
                notifyFailed(ERROR_AD_TYPE_IS_NULL);
                return;
            }
            AdController.this.fetchAd(family_id, user_id, ticket, adData -> {
                if (adData == null) {
                    notifyFailed(ERROR_CONNECT_SERVER);
                    return;
                }
                HashMap<String, AdEntity.RulesEntity> rulesMap = null;
                HashMap<String, HashMap<String, AdEntity.RulesEntity>> rules = adData.getRules();
                for (Map.Entry<String, HashMap<String, AdEntity.RulesEntity>> next : rules.entrySet()) {
                    if (mRuleType.equalsIgnoreCase(next.getKey())) {
                        rulesMap = next.getValue();
                        break;
                    }
                }

                if (rulesMap == null) {
                    notifyFailed(ERROR_NO_MATCHING_AD_TYPE);
                    return;
                }

                AdEntity.RulesEntity currentRule = getCurrentRule(rulesMap);
                if (currentRule == null) {
                    notifyNoExist();
                    return;
                }

                execute(adData.getAds(), currentRule);

            });
        }

        private void execute(List<HashMap<String, List<AdEntity.AdsEntity>>> ads, AdEntity.RulesEntity rule) {
            if (ads == null || rule == null || TextUtils.isEmpty(rule.getId()) || ads.size() == 0) {
                notifyNoExist();
                return;
            }

            if (TextUtils.isEmpty(rule.getTime_length())) {
                notifyFailed(ERROR_NO_TIME_LENGTH);
                return;
            }
            long totalTime = NumberUtil.convertTolong(rule.getTime_length(), -1L);
            if (totalTime == -1L) {
                notifyFailed(ERROR_NO_TIME_LENGTH);
                return;
            }

            List<AdEntity.AdsEntity> adEntities = null;
            for (HashMap<String, List<AdEntity.AdsEntity>> map : ads) {
                if (map == null)
                    continue;
                adEntities = map.get(rule.getId());
                if (adEntities != null)
                    break;
            }

            if (adEntities == null || adEntities.size() == 0) {
                notifyNoExist();
                return;
            }

            mInterrupt = false;

            //因为首页需要不停的播放, 所以把时间强制设置为一小时以上,这样就可以不间断获取
            if (TYPE_HOME.equalsIgnoreCase(mRuleType)) {
                totalTime = 60 * 70;
            }
//            totalTime = 5;  //测试用
            timer(totalTime, adEntities);

        }

        private void timer(long totalTime, List<AdEntity.AdsEntity> adEntities) {
            if (adEntities == null || adEntities.size() == 0)
                return;
//            int executeTime = NumberUtil.convertToint(currentTimeToRuleKey(), 0);
            mAlreadyExecuteTime = 0L;

            AdEntity.AdsEntity adsEntity = adEntities.get(getCurrentTimerIndex(adEntities));

            if (adsEntity == null) {
                mCurrentTimerIndex++;
                if (mCurrentTimerIndex == adEntities.size()) {
                    return;
                }
                timer(totalTime, adEntities);
                return;
            }
            long delay = NumberUtil.convertTolong(adsEntity.getRunning_time(), 0);
            oneTimer(delay, adsEntity, new ITimer() {
                @Override
                public void onTimerFinished(boolean success) {

                    if (mInterrupt) {
                        quit(true);
                        return;
                    }

                    if (success) {
                        mAlreadyExecuteTime += delay;
                    }

                    if (mAlreadyExecuteTime >= totalTime) {
                        quit(true);
                        return;
                    }

//                    if (NumberUtil.convertToint(currentTimeToRuleKey(), 0) != executeTime) {
//                        quit();
//                        AdController.this.executeAdRule(mRuleType, mFamilyId, mUrserId, mTicket, iExecuteRule);
//                        return;
//                    }

                    mCurrentTimerIndex++;
                    AdEntity.AdsEntity next = adEntities.get(getCurrentTimerIndex(adEntities));
                    if (next == null) {
                        mCurrentTimerIndex++;
                        timer(totalTime, adEntities);
                        return;
                    }
                    long nextDelay = NumberUtil.convertTolong(next.getRunning_time(), 0);
                    oneTimer(nextDelay, next, this);
                }


            });

        }


        public void interrupt() {
            mInterrupt = true;
        }

        private void quit() {
            quit(false);
        }

        private void quit(boolean shouldNotify) {
            if (shouldNotify) {
                notifyPlayFinished();
            }
            interrupt();
            mCurrentTimerIndex = 0;
            mAlreadyExecuteTime = 0L;
        }


        private void oneTimer(long delay, AdEntity.AdsEntity adsEntity, ITimer iTimer) {

            AdDownloadManager.getInstance().download(adsEntity.getMd5(), adsEntity.getMedia_url(), file -> {
                if (file == null) {
                    notifyAdDownloadError(adsEntity);
                    Observable.timer(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                if (iTimer != null)
                                    iTimer.onTimerFinished(false);
                            });
                    return;
                }
                notifyAdReceive(adsEntity, file);
                Observable.timer(delay, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            if (iTimer != null)
                                iTimer.onTimerFinished(true);
                        });
            });

        }

        private int getCurrentTimerIndex(List<AdEntity.AdsEntity> entities) {
            if (mCurrentTimerIndex >= entities.size())
                mCurrentTimerIndex = 0;
            return mCurrentTimerIndex;
        }

        private AdEntity.RulesEntity getCurrentRule(HashMap<String, AdEntity.RulesEntity> rulesMap) {
            String hour = currentTimeToRuleKey();
            return rulesMap.get(hour);
        }


        private String currentTimeToRuleKey() {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            return String.valueOf(hourOfDay);
        }

        private void notifyFailed(String reason) {
            if (iExecuteRule != null) {
                iExecuteRule.onAdExecuteFailed(reason);
            }
        }

        private void notifyNoExist() {
            if (iExecuteRule != null) {
                iExecuteRule.onAdNoExist(mRuleType, currentTimeToRuleKey());
            }
        }

        private void notifyAdDownloadError(AdEntity.AdsEntity adsEntity) {
            if (iExecuteRule != null) {
                iExecuteRule.onAdDownloadError(adsEntity);
            }
        }

        private void notifyAdReceive(AdEntity.AdsEntity adsEntity, File file) {
            if (iExecuteRule != null) {
                iExecuteRule.onAdReceive(adsEntity, file);
            }
        }

        private void notifyPlayFinished() {
            if (iExecuteRule != null) {
                iExecuteRule.onAdPlayFinished();
            }
        }


    }

}
