package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.event.ChangeVillageIdRefreshEvent;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.entity.event.RefreshStopEvent;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.mvp.v.fragment.ServiceFragment;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class ServiceFragmentPresenter extends AppBaseFragmentPresenter<ServiceFragment> {

    private Subscription mSubs1, mSubs2;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        reloadPage();
        reloadUrlAndRefreshWebPage();
    }

    //收到此消息,说明在侧边栏切换了家庭,那么此处就应该重新按照SP中存储的小区或者家庭的id来重载页面
    private void reloadUrlAndRefreshWebPage() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof ChooseFamilyVillageEvent) {
                                getView().getWebView().reload();
                            }
                        })
        );
    }

    private void reloadPage() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof ReloadWebPageEvent) {  //此方法没什么用了,只是在回复帖子之后使用
//                        getView().getWebView().reload();
                    } else if (o instanceof RefreshStopEvent) {  //此方法没有任何用了.之前是在JS中通知停止刷新
                        getView().stopRefresh("");
                    } else if (o instanceof ChangeVillageIdRefreshEvent) {
                        getView().reload();
                    }
                }));

    }

    public void onTitleRightClick() {

    }
}
