package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.os.Handler;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.v.activity.ChooseVillageFamilyActivity;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.SplashActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.user.UserManager;
import com.umeng.analytics.MobclickAgent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SplashActivityPresenter extends AppBaseActivityPresenter<SplashActivity> {
    @Override
    public void initData(Bundle savedInstanceState) {
        MobclickAgent.openActivityDurationTrack(false);

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public void jumpToMainActivity(int delay) {
        new Handler().postDelayed(() -> {
            if (UserManager.INSTANCE.isLogin()) {
                //判断是不是有家庭或者小区,有的话直接进首页,没有的话,就进ChooseFamilyVillageActivity
                if (UserManager.INSTANCE.hasFamily() || UserManager.INSTANCE.hasVillage()) {
                    new AcHelper.Builder(getView())
                            .target(TabHomeActivity.class)
                            .closeCurrent(true)
                            .enterAnim(R.anim.fade_in)
                            .exitAnim(R.anim.fade_out)
                            .start();
                } else {
                    new AcHelper.Builder(getView())
                            .target(ChooseVillageFamilyActivity.class)
                            .closeCurrent(true)
                            .enterAnim(R.anim.fade_in)
                            .exitAnim(R.anim.fade_out)
                            .start();
                }
            } else {
                new AcHelper.Builder(getView())
                        .target(LoginActivity.class)
                        .closeCurrent(true)
                        .enterAnim(R.anim.fade_in)
                        .exitAnim(R.anim.fade_out)
                        .start();
            }
            getView().setCanBack(true);
        }, delay);
    }
}

