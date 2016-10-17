package com.techjumper.polyhome_b.adlib.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome_b.adlib.db.utils.AdStatDbExecutor;
import com.techjumper.polyhome_b.adlib.entity.AdStatParamEntity;
import com.techjumper.polyhome_b.adlib.entity.BaseEntity;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStat;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStatTime;
import com.techjumper.polyhome_b.adlib.manager.AdController;
import com.techjumper.polyhome_b.adlib.net.NetHelper;
import com.techjumper.polyhome_b.adlib.net.RetrofitTemplate;
import com.techjumper.polyhome_b.adlib.utils.PollingUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/7/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdStatService extends Service {

    public static final String SP_NAME = "ad_sp";
    public static final String KEY_FAMILY_ID = "key_ad_family_id";
    public static final long ADSTAT_TIME_INTERVAL = 5 * 60 * 1000L;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            String familyId = PreferenceUtils.getPreference(SP_NAME).getString(KEY_FAMILY_ID, "");
            if (TextUtils.isEmpty(familyId)) {
                JLog.d("<ad> 没有familyId或者还没有执行过广告, 所以无法上报数据");
                return super.onStartCommand(intent, flags, startId);
            }

            AdStatDbExecutor.BriteDatabaseHelper dbHelper = AdStatDbExecutor.getHelper();

            dbHelper.getAdStatTime()
                    .map(adStatTime -> {
                        if (adStatTime == null) {
                            JLog.d("<ad> 还未有任何广告时间记录");
                            return false;
                        }

                        long lastTime = NumberUtil.convertTolong(adStatTime.time(), -1L);

                        boolean b = System.currentTimeMillis() - lastTime >= ADSTAT_TIME_INTERVAL || System.currentTimeMillis() <= lastTime;
                        String log = b ? "<ad> 到达上报时间, 开始上报广告" : "<ad> 未到上报广告时间";
                        JLog.d(log);
                        return b;
                    })
                    .filter(aBoolean -> aBoolean)
                    .flatMap(aBoolean1 -> dbHelper.queryAll())
                    .filter(adStats -> {
                        if (adStats != null && adStats.size() != 0) {
                            return true;
                        } else {
                            JLog.d("<ad> 没有可以上报的广告");
                            return false;
                        }
                    })
                    .flatMap(adStats1 -> {
                        String json = createParamJson(adStats1);
                        JLog.d("<ad> 准备上报广告数据: familyId=" + familyId + ", json=" + json);
                        return RetrofitTemplate.getInstance().adStat(familyId, json);
                    })
                    .subscribe(new Subscriber<BaseEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            JLog.d("<ad> 上报失败: " + e);
                        }

                        @Override
                        public void onNext(BaseEntity baseEntity) {
                            if (!NetHelper.isSuccess(baseEntity)) {
                                String reason = baseEntity == null ? "未知原因" : baseEntity.getError_msg();
                                JLog.d("<ad> 广告上报失败: " + reason);
                                return;
                            }

                            Observable
                                    .create(new Observable.OnSubscribe<Boolean>() {
                                        @Override
                                        public void call(Subscriber<? super Boolean> subscriber) {
                                            subscriber.onNext(dbHelper.deleteAll());
                                            subscriber.onCompleted();
                                        }
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(result -> {
                                        JLog.d("<ad> 上报成功");
                                        dbHelper.close();
                                    });
                        }
                    });
        } finally {
//            PollingUtils.stopPollingService(Utils.appContext
//                    , AdStatService.class, "", AdController.CODE_AD_STAT);
            PollingUtils.startPollingServiceBySet(Utils.appContext
                    , AdController.getAdStatUploadNextTime()
                    , AdStatService.class, "", true, AdController.CODE_AD_STAT, true);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static Observable<AdStatTime> nextAdStatTime() {
        AdStatDbExecutor.BriteDatabaseHelper dbHelper = AdStatDbExecutor.getHelper();
        return dbHelper.getAdStatTime();
    }

    private String createParamJson(List<AdStat> adStats) {

        if (adStats == null || adStats.isEmpty()) {
            return "";
        }

        AdStatParamEntity entity = new AdStatParamEntity();
        List<AdStatParamEntity.AdsEntity> adsEntities = new ArrayList<>();
        entity.setAds(adsEntities);

        for (AdStat adStat : adStats) {
            AdStatParamEntity.AdsEntity adsEntity = new AdStatParamEntity.AdsEntity();
            adsEntity.setAd_id(adStat.adId() + "");
            adsEntity.setCount(adStat.count() + "");
            adsEntity.setPosition(adStat.position() + "");
            adsEntities.add(adsEntity);
        }

        return GsonUtils.toJson(entity);
    }
}
