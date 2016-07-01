package com.techjumper.polyhome_b.adlib.net;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.rx.tools.SchedulersCompat;
import com.techjumper.lib2.others.Config;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RetrofitTemplate {

    private static RetrofitTemplate INSTANCE;

    private RetrofitTemplate() {
        Config.sDefaultBaseUrl = com.techjumper.polyhome_b.adlib.Config.sBaseUrl;
        RetrofitHelper.sDefaultInterface = ServiceAPI.class;
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
        return RetrofitHelper.<ServiceAPI>createDefault()
                .padAd(NetHelper.createBaseArgumentsMap(
                        KeyValueCreator.padAd(family_id, user_id, ticket)))
                .compose(SchedulersCompat.applyExecutorSchedulers());
    }

    public Observable<ResponseBody> donwloadFile(String url) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .downloadFile(url)
                .compose(CommonWrap.wrap());
    }

}
