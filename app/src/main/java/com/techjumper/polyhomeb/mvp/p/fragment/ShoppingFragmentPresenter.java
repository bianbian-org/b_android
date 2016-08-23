package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.event.RefreshStopEvent;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.mvp.v.fragment.ShoppingFragment;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ShoppingFragmentPresenter extends AppBaseFragmentPresenter<ShoppingFragment> {

    private Subscription mSub1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        reloadPage();
    }

    private void reloadPage() {
        RxUtils.unsubscribeIfNotNull(mSub1);
        addSubscription(
                mSub1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof ReloadWebPageEvent) {
                        getView().getWebView().reload();
                    } else if (o instanceof RefreshStopEvent) {
                        getView().stopRefresh("");
                    }
                }));

    }
}
