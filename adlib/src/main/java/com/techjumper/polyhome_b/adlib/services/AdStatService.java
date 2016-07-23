package com.techjumper.polyhome_b.adlib.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome_b.adlib.db.utils.AdStatDbExecutor;
import com.techjumper.polyhome_b.adlib.entity.AdStatParamEntity;
import com.techjumper.polyhome_b.adlib.entity.BaseEntity;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStat;
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        PollingUtils.startPollingServiceBySet(Utils.appContext
                , System.currentTimeMillis() + AdController.AD_STAT_INTERVAL
                , AdStatService.class, "", true, AdController.CODE_AD_STAT, true);

        String familyId = PreferenceUtils.getPreference(SP_NAME).getString(KEY_FAMILY_ID, "");
        if (TextUtils.isEmpty(familyId)) {
            JLog.d("<ad> 没有familyId或者还没有执行过广告, 所以无法上报数据");
            return super.onStartCommand(intent, flags, startId);
        }
        AdStatDbExecutor.BriteDatabaseHelper dbHelper = AdStatDbExecutor.getHelper();
        dbHelper.queryAll()
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
                        JLog.d("<ad> 服务器上报失败: " + e);
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
                                    if (result) {
                                        JLog.d("<ad> 上报成功, 并清除本地数据库");
                                    } else {
                                        JLog.d("<ad> 上报成功, 但是清理本地数据库失败");
                                    }
                                });
                    }
                });


        return super.onStartCommand(intent, flags, startId);
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
            adsEntity.setAd_id(adStat._id() + "");
            adsEntity.setCount(adStat.count() + "");
            adsEntities.add(adsEntity);
        }

        return GsonUtils.toJson(entity);
    }
}
