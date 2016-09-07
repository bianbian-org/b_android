package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.mvp.m.ReplyDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyDetailActivity;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ReplyDetailActivityPresenter extends AppBaseActivityPresenter<ReplyDetailActivity> {

    private ReplyDetailActivityModel mModel = new ReplyDetailActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof ReloadWebPageEvent) {
                ReloadWebPageEvent event = (ReloadWebPageEvent) o;
                if (getView().webViewIsInit()) {
                    getView().getWebView().reload();
                }
            }
        }));
    }

    public String getUrl() {
        return mModel.getUrl();
    }

}
