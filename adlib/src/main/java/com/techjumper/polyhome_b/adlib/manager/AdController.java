package com.techjumper.polyhome_b.adlib.manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.corelib.utils.system.PowerUtil;
import com.techjumper.polyhome_b.adlib.db.utils.AdStatDbExecutor;
import com.techjumper.polyhome_b.adlib.download.AdDownloadManager;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.services.AdStatService;
import com.techjumper.polyhome_b.adlib.services.AlarmService;
import com.techjumper.polyhome_b.adlib.services.WakeupAdService;
import com.techjumper.polyhome_b.adlib.utils.PollingUtils;

import java.io.File;
import java.util.ArrayList;
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

    private static int mLockTime;
    private volatile static AdController INSTANCE;

    public static final int CODE_WAKEUP_ALARM = 99; //唤醒广告服务的requestCode;
    public static final int CODE_ALARM_SERVICE = 100;
    public static final int CODE_AD_STAT = 101; //广告统计;

    public static final long AD_STAT_INTERVAL = 2 * 60 * 1000L; //检查广告上报的时间;

    public static final String TYPE_HOME = "1001"; //'室内机 - 首页'
    public static final String TYPE_VIDEO = "1002";  //室内机 - 视频通话
    public static final String TYPE_SLEEP = "1003"; //室内机 - 休眠
    public static final String TYPE_WAKEUP = "1004"; //室内机 - 唤醒
    public static final String TYPE_HOME_TWO = "1005"; //室内机 - 首页二屏

    //    public static final String DOWNLOAD_SUB_PATH = "mediacache";
    private Subscription mPadAdSubs;
    private HashMap<String, AdRuleExecutor> mExecutorMap = new HashMap<>();

    private IWakeUp iWakeUP;
    private ScreenOffReceiver mScreenOffReceiver;
    private AlarmReceiver mAlarmReceiver = new AlarmReceiver();
    private WakeUpReceiver mWakeUpReceiver = new WakeUpReceiver();

    private ArrayList<IAlarm> iAlarmListeners = new ArrayList<>();
    /**
     * 是否开始上传广告
     */
    private static boolean sIsStartedUploadAd;

    Subscription subscribe = null;
    private int count;

    private AdController() {

    }

    public static AdController getInstance() {
        if (INSTANCE == null) {
            synchronized (AdController.class) {
                if (INSTANCE == null) {
                    init();
                    INSTANCE = new AdController();
                }
            }
        }
        return INSTANCE;
    }

    public static void init() {
        mLockTime = 3 * 60 * 1000;
        setScreenOffTime(mLockTime);
        //开启广告定时统计
        PollingUtils.stopPollingService(Utils.appContext
                , AdStatService.class, "", CODE_AD_STAT);
        PollingUtils.startPollingServiceBySet(Utils.appContext
                , getAdStatUploadNextTime()
                , AdStatService.class, "", true, AdController.CODE_AD_STAT, true);
        sIsStartedUploadAd = true;
    }

    public static boolean isStartedUploadAd() {
        return sIsStartedUploadAd;
    }

    public static long getAdStatUploadNextTime() {
        return System.currentTimeMillis() + AdController.AD_STAT_INTERVAL;
    }

    public boolean wakeUpScreen() {
        if (isScreenOn()) {
            JLog.d("屏幕已经是亮的所以不做操作");
            return false;
        }
        JLog.d("唤醒屏幕");
//        setScreenOffTime(mLockTime);
        PowerUtil.wakeUpScreen();
        return true;
    }

    /**
     * 取消带窗口的广告
     */
    public void interruptWindowAd() {
        interrupt(TYPE_SLEEP);
        interrupt(TYPE_WAKEUP);
    }

    @SuppressLint("NewApi")
    public boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) Utils.appContext.getSystemService(Context.POWER_SERVICE);
        return Build.VERSION.SDK_INT >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();

    }

    public void stopAdTimer(String adType) {
        if (adType == null) return;
        AdRuleExecutor executor = mExecutorMap.get(adType);
        if (executor != null) {
            executor.stopTimer();
        }
    }

    public void startAdTimer(String adType, int index) {
        if (adType == null) return;
        AdRuleExecutor executor = mExecutorMap.get(adType);
        if (executor != null) {
            executor.startTimerWithIndex(index);
        }
    }

    public synchronized void startWakeUpTimer(IWakeUp iWakeUp) {
        stopWakeUpTimer();
        this.iWakeUP = iWakeUp;
        IntentFilter intentFilter = new IntentFilter(WakeupAdService.ACTION_WAKE_UP);
        Utils.appContext.registerReceiver(mWakeUpReceiver, intentFilter);
        //            Calendar c = Calendar.getInstance();
//            long oneHourLater = System.currentTimeMillis() + 1000L * 60 * 60;
        long triggerTime = getTriggerTime();
//            c.setTimeInMillis(oneHourLater);
//            c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//            c.set(Calendar.MINUTE, 0);
//            c.set(Calendar.SECOND, 0);
//            c.set(Calendar.MILLISECOND, 0);

//        PollingUtils.startPollingService(Utils.appContext
//                , triggerTime, 60 * 60L, WakeupAdService.class, "", CODE_WAKEUP_ALARM);
        PollingUtils.startPollingServiceBySet(Utils.appContext
                , triggerTime, WakeupAdService.class, "", true, CODE_WAKEUP_ALARM, true);
//        if (this.iWakeUP != null) {
//            this.iWakeUP.onWakeUpAdExecute();
//        }
//        PollingUtils.startPollingService(Utils.appContext
//                , oneHourLater, 5L, WakeupAdService.class, "", CODE_WAKEUP_ALARM);
    }

    public void stopWakeUpTimer() {
        try {
            Utils.appContext.unregisterReceiver(mWakeUpReceiver);
        } catch (Exception ignored) {
        }
        PollingUtils.stopPollingService(Utils.appContext
                , WakeupAdService.class, "", CODE_WAKEUP_ALARM);
        iWakeUP = null;
    }


    public synchronized void startPolling(IAlarm iAlarm, boolean fromCache) {
//        stopPolling(iAlarm);
        clearPolling();
        addAlarmListener(iAlarm);
        IntentFilter filter = new IntentFilter(AlarmService.ACTION_ALARM_SERVICE);
        Utils.appContext.registerReceiver(mAlarmReceiver, filter);

        long timeMillis = getTriggerTime();

        PollingUtils.startPollingServiceBySet(Utils.appContext
                , timeMillis, AlarmService.class, "", true, CODE_ALARM_SERVICE, true);
        notifyAlarmListener(fromCache);
//        PollingUtils.startPollingService(Utils.appContext
//                , timeMillis, 30L, AlarmService.class, "", CODE_ALARM_SERVICE);

    }

    private void addAlarmListener(IAlarm iAlarm) {
        for (IAlarm alarm : iAlarmListeners) {
            if (alarm == iAlarm) return;
        }
        iAlarmListeners.add(iAlarm);
    }

    private void removeAlarmListener(IAlarm iAlarm) {
        Iterator<IAlarm> it = iAlarmListeners.iterator();
        while (it.hasNext()) {
            IAlarm next = it.next();
            if (next == iAlarm) {
                it.remove();
                return;
            }
        }
    }

    private void clearAlarmListener() {
        iAlarmListeners.clear();
    }

    private void notifyAlarmListener(boolean fromCache) {
        for (IAlarm listener : iAlarmListeners) {
            if (listener != null) {
                listener.onAlarmReceive(fromCache);
            }
        }
    }

    public void clearPolling() {
        try {
            Utils.appContext.unregisterReceiver(mAlarmReceiver);
        } catch (Exception ignored) {
        }
        PollingUtils.stopPollingService(Utils.appContext
                , AlarmService.class, "", CODE_ALARM_SERVICE);
        clearAlarmListener();
    }

    public void stopPolling(IAlarm alarm) {
        removeAlarmListener(alarm);
    }

    private long getTriggerTime() {
        Calendar c = Calendar.getInstance();
        long oneHourLater = System.currentTimeMillis() + 1000L * 60 * 60;
//        long oneHourLater = System.currentTimeMillis() + 1000L * 5;
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        c.setTimeInMillis(oneHourLater);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
//        return oneHourLater;
        return c.getTimeInMillis();
    }

    public synchronized void receiveScreenOff(IScreenOff iScreenOff) {
        stopReceiveScreenOff();
        mScreenOffReceiver = new ScreenOffReceiver(iScreenOff, mLockTime);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        Utils.appContext.registerReceiver(mScreenOffReceiver, intentFilter);
        JLog.d("开始接收屏幕关闭的消息");
    }

//    public void resetSleepTime() {
//        JLog.d("恢复休眠时间: " + mLockTime);
//        setScreenOffTime(mLockTime);
//    }

    public void stopReceiveScreenOff() {
        try {
            Utils.appContext.unregisterReceiver(mScreenOffReceiver);
        } catch (Exception ignored) {
        }
        JLog.d("停止接收屏幕关闭的消息");
    }


    public void executeAdRule(String adType, String family_id, String user_id, String ticket, boolean fromCache, IExecuteRule iExecuteRule) {
        if (TextUtils.isEmpty(adType)) return;
        AdRuleExecutor executor;
        synchronized (this) {
            cancel(adType);
            cancel(TYPE_HOME);
            cancel(TYPE_HOME_TWO);
            cancel(TYPE_VIDEO);
            executor = new AdRuleExecutor(adType);
            mExecutorMap.put(adType, executor);
        }
        executor.fetchAd(family_id, user_id, ticket, fromCache, iExecuteRule);
    }

    public void cancelAll() {
        for (Map.Entry<String, AdRuleExecutor> next : mExecutorMap.entrySet()) {
            if (next == null) continue;
            AdRuleExecutor executor = next.getValue();
            if (executor != null) {
                executor.quit(true);
            }
        }
        mExecutorMap.clear();
    }

    public void cancel(String adType) {
        if (adType == null) return;
        AdRuleExecutor executor = mExecutorMap.get(adType);
        if (executor != null) {
            executor.quit(true);
            mExecutorMap.put(adType, null);
        }
    }

    public void interrupt(String adType) {
        if (adType == null) return;
        AdRuleExecutor executor = mExecutorMap.get(adType);
        if (executor != null) {
            executor.quit(false);
            mExecutorMap.put(adType, null);
        }
    }


    private void fetchAd(String family_id, String user_id, String ticket, boolean fromCache, IFetchAd iFetchAd) {
//        RxUtils.unsubscribeIfNotNull(mPadAdSubs);
        mPadAdSubs = AdListManager.getInstance().fetchAdList(family_id, user_id, ticket, fromCache)
                .subscribe(new Subscriber<AdEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        JLog.d(e);
                        onNext(AdListManager.getInstance().getLocalAdEntity());
//                        ToastUtils.show(Utils.appContext.getString(R.string.error_to_connect_server));
                    }

                    @Override
                    public void onNext(AdEntity adEntity) {
                        if (adEntity == null) {
                            JLog.d("adEntity is null");
//                            ToastUtils.show(Utils.appContext.getString(R.string.error_to_connect_server));
                            return;
                        }

                        JLog.d("<ad> 获取广告信息成功: " + adEntity.toString());

                        if (iFetchAd != null) {
                            iFetchAd.onAdInfoReceive(adEntity);
                        } else {
                            JLog.d("<ad> iFetchAd 为空, 无法回调");
                        }
                    }
                });
    }


    public void cancelFetchAdFromServer() {
        RxUtils.unsubscribeIfNotNull(mPadAdSubs);
    }


    public void turnOffScreen() {
//        int debounce = 4 * 60 * 1000;
//        if (debounce == mLockTime) {
//            debounce -= 60 * 1000;
//        }
//        if (mScreenOffReceiver == null) {
//            throw new NullPointerException("please call receiveScreenOff() first");
//        }
//        receiveScreenOff(mScreenOffReceiver.getScreenOffListener());
        Utils.appContext.sendBroadcast(new Intent("action_bhost_lock_screen"));
    }

//    private static int getScreenOffTime() {
//        int screenOffTime = -2;
//        try {
//            screenOffTime = Settings.System.getInt(Utils.appContext.getContentResolver(),
//                    Settings.System.SCREEN_OFF_TIMEOUT);
//        } catch (Exception e) {
//            JLog.d("获取系统时间失败: " + e);
//        }
////        JLog.d("当前系统休眠时间为: " + screenOffTime);
//        return screenOffTime;
//    }

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

    public boolean isRunning(String type) {
        AdRuleExecutor executor = mExecutorMap.get(type);
        return executor != null && !executor.isInterrupt();

    }

    public interface IFetchAd {
        void onAdInfoReceive(AdEntity adData);
    }

    public static abstract class IExecuteRule {

        public void onAllAdsReceive(List<AdEntity.AdsEntity> allAds) {
        }

        public abstract void onAdReceive(AdEntity.AdsEntity adsEntity, File file);

        public abstract void onAdPlayFinished();

        public abstract void onAdDownloadError(AdEntity.AdsEntity adsEntity);

        public abstract void onAdExecuteFailed(String reason);

        public abstract void onAdNoExist(String adType, String hour);
    }

    public interface ITimer {
        void onTimerFinished(boolean success);
    }

    public interface IWakeUp {
        void onWakeUpAdExecute();
    }

    public interface IAlarm {
        void onAlarmReceive(boolean fromCache);
    }

    public interface IScreenOff {
        void onSleepAdExecute();
    }

    public class ScreenOffReceiver extends BroadcastReceiver {

        private IScreenOff iScreenOff;
        private int mScreenOffTime;

        public ScreenOffReceiver(IScreenOff iScreenOff, int screenOffTime) {
            this.iScreenOff = iScreenOff;
            mScreenOffTime = screenOffTime;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            JLog.d("屏幕关闭了");
            //只要是休眠就停首页广告
            cancel(TYPE_HOME);
            cancel(TYPE_HOME_TWO);
            cancel(TYPE_VIDEO);

            AdRuleExecutor wakeUpExecutor = mExecutorMap.get(AdController.TYPE_WAKEUP);
            if (wakeUpExecutor != null) {
                if (!wakeUpExecutor.isInterrupt())
                    JLog.d("已经在执行唤醒广告, 所以不再发布休眠消息");
                else {
                    JLog.d("唤醒广告执行完毕, 所以关闭屏幕后不再发布休眠消息");
                    mExecutorMap.put(TYPE_WAKEUP, null);
                }
                interrupt(TYPE_SLEEP);
                return;
            }

//            if (getScreenOffTime() != mScreenOffTime) {
//                JLog.d("代码控制关闭屏幕, 不触发回调; getScreenOffTime()=" + getScreenOffTime() + ", mScreenOffTime=" + mScreenOffTime);
//                setScreenOffTime(mScreenOffTime);
//                return;
//            }


            AdRuleExecutor sleepExecutor = mExecutorMap.get(AdController.TYPE_SLEEP);
            if (sleepExecutor != null && sleepExecutor.isSleepingFinished()) {
                JLog.d("已经播完了休眠广告, 不再重复触发休眠");
                mExecutorMap.put(TYPE_SLEEP, null);
                return;
            }


            if (iScreenOff != null) {
                iScreenOff.onSleepAdExecute();
            }
        }

        public IScreenOff getScreenOffListener() {
            return iScreenOff;
        }
    }

    public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            notifyAlarmListener(true);
        }
    }

    public class WakeUpReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (iWakeUP != null) {
                iWakeUP.onWakeUpAdExecute();
            }
        }
    }

    private class AdRuleExecutor {
        public static final String ERROR_CONNECT_SERVER = "无法获取广告数据";
        public static final String ERROR_DOWNLOAD_AD_ERROR = "下载广告失败";
        public static final String ERROR_AD_TYPE_IS_NULL = "广告类型为 null";
        public static final String ERROR_NO_MATCHING_AD_TYPE = "没有匹配的广告类型";
        public static final String ERROR_NO_TIME_LENGTH = "未指定持续时间";
        public static final String ERROR_NO_ADS = "无广告数据";

        private String mUniqueId;
        private IExecuteRule iExecuteRule;
        private String mRuleType;
        private String mFamilyId;
        private String mUrserId;
        private String mTicket;
        private int mCurrentTimerIndex;
        private long mAlreadyExecuteTime;
        private boolean mInterrupt;
        private Subscription mTimerSubs;
        /**
         * 是否是休眠播放完成
         */
        private boolean mIsSleepFinished;
        private long mTotalTime;
        private List<AdEntity.AdsEntity> mAdEntities;

        public AdRuleExecutor(String ruleType) {

            //用一个随机值来唯一标识一个任务
            mUniqueId = Math.random() * 100000 + "";

            mRuleType = ruleType;
            if (mRuleType == null) {
                mRuleType = "";
            }
        }

        private AdStatDbExecutor.BriteDatabaseHelper getDbHelper() {
            return AdStatDbExecutor.getHelper();
        }

        public void stopTimer() {
            RxUtils.unsubscribeIfNotNull(mTimerSubs);
        }

        public void startTimerWithIndex(int index) {
            stopTimer();
            setCurrentTimerIndex(index);
            if (mAdEntities == null || mAdEntities.size() == 0 || mTotalTime == 0L)
                return;
            timer(mTotalTime, mAdEntities);
        }

        public void fetchAd(String family_id, String user_id, String ticket, boolean fromCache, IExecuteRule iExecuteRule) {
            this.mFamilyId = family_id;
            this.mUrserId = user_id;
            this.mTicket = ticket;

            this.iExecuteRule = iExecuteRule;
            if (TextUtils.isEmpty(mRuleType)) {
                notifyFailed(ERROR_AD_TYPE_IS_NULL);
                return;
            }
            //保存familyId到sp,用于广告统计
            JLog.d("更新familyId, 用于广告统计: " + family_id);
            PreferenceUtils.getPreference(AdStatService.SP_NAME).edit().putString(AdStatService.KEY_FAMILY_ID, family_id).commit();

            AdController.this.fetchAd(family_id, user_id, ticket, fromCache, adData -> {
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

            //将播放的广告统计到数据库
            String adId = rule.getId();

            //因为首页需要不停的播放, 所以把时间强制设置为一小时以上,这样就可以不间断获取
            if (isHomeAdType(mRuleType)) {
                totalTime = 60 * 70L;
            }
//            totalTime = 5;  //测试用

            notifyAllAdsReceive(adEntities);
            timer(totalTime, adEntities);

        }

        private boolean isHomeAdType(String ruleType) {
            return TYPE_HOME.equalsIgnoreCase(mRuleType) || TYPE_HOME_TWO.equalsIgnoreCase(mRuleType);
        }

        private void saveAdStatToDb(String id, String type) {
            AdStatDbExecutor.BriteDatabaseHelper dbHelper = getDbHelper();
            dbHelper.increase(id, type)
                    .flatMap(aLong -> dbHelper.query(id, type))
                    .subscribe(adStat -> {
                        if (adStat == null) {
                            JLog.d("<ad> 查找ID为" + id + ", position为" + type + " 的广告失败");
                            return;
                        }
                        //如果可以的话,更新广告时间
                        dbHelper.updateTimeIfNotExist(System.currentTimeMillis());

                        JLog.d("<ad> 统计广告次数, id=" + id + ", position=" + type
                                + ", count=" + adStat.count() + ", mUniqueId=" + mUniqueId);
                    });
        }

        private void timer(long totalTime, List<AdEntity.AdsEntity> adEntities) {
            if (adEntities == null || adEntities.size() == 0)
                return;
//            int executeTime = NumberUtil.convertToint(currentTimeToRuleKey(), 0);
            mAlreadyExecuteTime = 0L;

            mTotalTime = totalTime;
            mAdEntities = adEntities;

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
            final int executeTime = NumberUtil.convertToint(currentTimeToRuleKey(), 0);
            oneTimer(delay, adsEntity, new ITimer() {
                @Override
                public void onTimerFinished(boolean success) {

                    if (mInterrupt) {
                        return;
                    }
                    if (success) {
                        mAlreadyExecuteTime += delay;
                    }

//                    if (TYPE_SLEEP.equalsIgnoreCase(mRuleType)) {
//                        JLog.d("休眠广告: totalTime=" + totalTime + ", alreadyExecuteTime=" + mAlreadyExecuteTime);
//                    }

                    if (mAlreadyExecuteTime >= totalTime) {
                        if (TYPE_SLEEP.equals(mRuleType)) {
                            mIsSleepFinished = true;
                        }
                        quit(true);
                        return;
                    }

                    if (NumberUtil.convertToint(currentTimeToRuleKey(), 0) != executeTime) {
                        AdController.this.executeAdRule(mRuleType, mFamilyId, mUrserId, mTicket, true, iExecuteRule);
                        return;
                    }

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


        private void interrupt() {
            iExecuteRule = null;
            mInterrupt = true;
        }

        public boolean isInterrupt() {
            return mInterrupt;
        }

        public boolean isSleepingFinished() {
            return mIsSleepFinished;
        }

        public void quit() {
            quit(false);
        }

        public void quit(boolean shouldNotify) {
            mInterrupt = true;
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
                    mTimerSubs = Observable.timer(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                if (iTimer != null)
                                    iTimer.onTimerFinished(false);
                            });
                    return;
                }

                notifyAdReceive(adsEntity, file);
                //保存广告ID到数据库
                saveAdStatToDb(adsEntity.getId(), mRuleType);
                mTimerSubs = Observable.timer(delay, TimeUnit.SECONDS)
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

        private void setCurrentTimerIndex(int index) {
            mCurrentTimerIndex = index;
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
            if (TYPE_WAKEUP.equals(mRuleType)) {
                mExecutorMap.put(TYPE_WAKEUP, null);
            }
            interrupt();
        }

        private void notifyNoExist() {
            if (iExecuteRule != null) {
                iExecuteRule.onAdNoExist(mRuleType, currentTimeToRuleKey());
            }
            if (TYPE_WAKEUP.equals(mRuleType)) {
                mExecutorMap.put(TYPE_WAKEUP, null);
            }
            interrupt();
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

        private void notifyAllAdsReceive(List<AdEntity.AdsEntity> allAds) {
            if (iExecuteRule != null) {
                iExecuteRule.onAllAdsReceive(allAds);
            }
        }

    }

}
