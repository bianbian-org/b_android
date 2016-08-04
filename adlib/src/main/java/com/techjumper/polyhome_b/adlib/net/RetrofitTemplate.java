package com.techjumper.polyhome_b.adlib.net;

import com.techjumper.corelib.rx.ExecutorManager;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.rx.tools.SchedulersCompat;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.entity.BaseEntity;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RetrofitTemplate {

    private static RetrofitTemplate INSTANCE;

    private RetrofitTemplate() {

//        Config.sDefaultBaseUrl = com.techjumper.polyhome_b.adlib.Config.sBaseUrl;
//        if (RetrofitHelper.sDefaultInterface == null) {
//            RetrofitHelper.sDefaultInterface = ServiceAPI.class;
//        }
    }

    public static RetrofitTemplate getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitTemplate.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitTemplate();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<AdEntity> padAd(String family_id, String user_id, String ticket) {
        return getDefault()
                .padAd(NetHelper.createBaseArgumentsMap(KeyValueCreator.padAd(family_id, user_id, ticket)))
                .subscribeOn(Schedulers.from(ExecutorManager.eventExecutor));
    }

    public Observable<ResponseBody> donwloadFile(String url) {
        return getDefault()
                .downloadFile(url)
                .compose(CommonWrap.wrap());
    }

    public Observable<BaseEntity> adStat(String family_id, String json) {
        return getDefault()
                .adStat(NetHelper.createBaseArguments(
                        KeyValueCreator.adStat(family_id, json)))
                .compose(SchedulersCompat.applyExecutorSchedulers());
    }

    public ServiceAPI getDefault() {
        return RetrofitHelper.createWithNew(com.techjumper.polyhome_b.adlib.Config.sBaseUrl
                , com.techjumper.polyhome_b.adlib.net.ServiceAPI.class);
    }
}
