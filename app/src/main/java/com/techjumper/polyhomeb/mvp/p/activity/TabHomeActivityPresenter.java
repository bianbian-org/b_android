package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.widget.PolyTab;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class TabHomeActivityPresenter extends AppBaseActivityPresenter<TabHomeActivity>
        implements PolyTab.ITabClick {

    private boolean mCanExit;
    private Subscription mSubs1;

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
        getView().getHomeViewPager().setCurrentItem(index, false);
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
//        addSubscription(
//                RxPermissions.getInstance(getView())
//                        .request(Manifest.permission.VIBRATE)
//                        .subscribe()
//        );
    }

}
