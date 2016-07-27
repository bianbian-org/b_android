package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.widget.PolyTab;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class TabHomeActivityPresenter extends AppBaseActivityPresenter<TabHomeActivity>
        implements PolyTab.ITabClick {

    private boolean mCanExit;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        requestPermission();
    }

    @OnClick({R.id.iv_left_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_icon:
                getView().toggleMenu();
                break;
        }
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
