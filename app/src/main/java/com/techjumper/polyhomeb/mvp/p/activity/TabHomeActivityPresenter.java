package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

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
            ToastUtils.show("再按一次, 退出应用");
            mCanExit = true;
            new Handler().postDelayed(() -> mCanExit = false, 2000);
            return;
        }

        getView().supportFinishAfterTransition();

    }
}
