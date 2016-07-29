package com.techjumper.polyhome_b.adlib.manager;

import android.os.SystemClock;
import android.text.TextUtils;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome_b.adlib.download.AdDownloadManager;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.net.RetrofitTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdListManager {

    private static final String AD_DATA_DIR = "ad";
    private static final String AD_DATA_NAME = "addata";
    private static final String SP_NAME = "adlist";
    private static final String KEY_LAST_TIME = "key_last_time";
    private static File mAdFile = new File(Utils.appContext.getCacheDir().getAbsolutePath() + File.separator + AD_DATA_DIR);


    /**
     * 过期时间
     * 天  时   分   秒    毫秒
     */
    private static long sCacheInterval = 2 * 24 * 60 * 60 * 1000L;


    private AdListManager() {
    }

    private static class SingletonInstance {
        private static final AdListManager INSTANCE = new AdListManager();
    }

    public static AdListManager getInstance() {
        return SingletonInstance.INSTANCE;
    }


    public Observable<AdEntity> fetchAdList(String familyId, String userId, String ticket, boolean fromCache) {
        if (fromCache && !isExpired()) {
            return fromCache(familyId, userId, ticket);
        } else {
            return fromNet(familyId, userId, ticket);
        }
    }

    private Observable<AdEntity> fromCache(String familyId, String userId, String ticket) {
        return Observable
                .create(new Observable.OnSubscribe<AdEntity>() {
                    @Override
                    public void call(Subscriber<? super AdEntity> subscriber) {
                        subscriber.onNext(getLocalAdEntity());
                        subscriber.onCompleted();
                    }
                })
                .flatMap(adEntity -> {
                    if (adEntity == null) {
                        return fromNet(familyId, userId, ticket);
                    }
                    return Observable.just(adEntity);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<AdEntity> fromNet(String familyId, String userId, String ticket) {
        JLog.d("<ad> 从网络获取广告列表");
        return RetrofitTemplate.getInstance().padAd(familyId, userId, ticket)
                .retryWhen(observable -> observable.map(e -> {
                    JLog.d(e);
                    return getLocalAdEntity();
                }))
                .map(adEntity -> {
                    if (adEntity == null) {
                        return getLocalAdEntity();
                    }

                    saveJsonToLocal(GsonUtils.toJson(adEntity));
                    cacheFile(adEntity);
                    PreferenceUtils.getPreference(SP_NAME).edit()
                            .putLong(KEY_LAST_TIME, System.currentTimeMillis()).apply();
                    return adEntity;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private AdEntity getLocalAdEntity() {
        JLog.d("<ad> 从本地获取广告列表");
        String data = getJsonFromLocal();
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        return GsonUtils.fromJson(data, AdEntity.class);
    }

    private boolean isExpired() {
        long lastTime = PreferenceUtils.getPreference(SP_NAME).getLong(KEY_LAST_TIME, 0L);
        return System.currentTimeMillis() - lastTime - sCacheInterval >= 0L;
    }

    private String getJsonFromLocal() {
        return FileUtils.loadTextFile(getAdDataPath(), AD_DATA_NAME);
    }

    private boolean saveJsonToLocal(String json) {
        return FileUtils.saveStringToPath(json, getAdDataPath(), AD_DATA_NAME);
    }

    private String getAdDataPath() {
        return mAdFile.getAbsolutePath();
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
                    SystemClock.sleep(3);
                    AdDownloadManager.getInstance().download(adsEntity.getMd5(), media_url, null);
                }

            }
        }
    }
}