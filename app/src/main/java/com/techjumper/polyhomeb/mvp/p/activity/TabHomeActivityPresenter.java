package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.user.event.LoginEvent;
import com.techjumper.polyhomeb.utils.LoginHelper;
import com.techjumper.polyhomeb.widget.PolyTab;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class TabHomeActivityPresenter extends AppBaseActivityPresenter<TabHomeActivity>
        implements PolyTab.ITabClick {

    private boolean mCanExit;
    private Subscription mSubs1, mLoginSubscription, mLoginEventSubs;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        requestPermission();
        onToggleMenuClick();
    }

    private void onToggleMenuClick() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof ToggleMenuClickEvent) {
                        if (getView().getSlidingMenu() != null) {
                            getView().toggleMenu();
                        }
                    }
                }));
    }

    @Override
    public void onTabClick(int index) {
        if (getView().getHomeViewPager().getCurrentItem() == index) return;
        if (index != 2) {
            processLogin(index);
            return;
        }
        getView().getHomeViewPager().setCurrentItem(index, false);
    }

    public void processLogin(int index) {
        RxUtils.unsubscribeIfNotNull(mLoginSubscription);
        registerLogEvent(false);
        mLoginSubscription = LoginHelper.processLogin(getView())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        JLog.d(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        if (!(o instanceof LoginEvent)) return;

                        LoginEvent event = (LoginEvent) o;
                        if (event.isLogin()) {
                            getView().onTabChange(index);
                        } else {
                            getView().onTabChange(2);
                        }
                        RxUtils.unsubscribeIfNotNull(mLoginSubscription);
                        registerLogEvent(true);
                    }
                });
    }

    private void registerLogEvent(boolean register) {
        RxUtils.unsubscribeIfNotNull(mLoginEventSubs);
        if (!register) return;
        addSubscription(
                mLoginEventSubs = RxBus.INSTANCE.asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Object>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Object o) {
                                if (!(o instanceof LoginEvent)) return;

                                LoginEvent event = (LoginEvent) o;
                                if (event.isLogin()) {
                                    getView().onTabChange(0);
                                } else {
                                    getView().onLogout();
                                    getView().onTabChange(2);
                                }
                            }
                        })
        );
    }

    public void onBackPressed() {

        if (getView().mSlidingMenu.isMenuShowing()) {
            getView().toggleMenu();
            return;
        }

        if (!mCanExit) {
            ToastUtils.show(getView().getString(R.string.exit_app));
            mCanExit = true;
            new Handler().postDelayed(() -> mCanExit = false, 2000);
            return;
        }

        getView().supportFinishAfterTransition();

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 16) {
            addSubscription(
                    RxPermissions.getInstance(getView())
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    , Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe()
            );
        } else {
            addSubscription(
                    RxPermissions.getInstance(getView())
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe()
            );
        }
    }

}
