package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.os.Handler;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.mvp.m.SplashActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChooseVillageFamilyActivity;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.SplashActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.user.UserManager;
import com.umeng.analytics.MobclickAgent;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SplashActivityPresenter extends AppBaseActivityPresenter<SplashActivity> {

    private SplashActivityModel mModel = new SplashActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {
        MobclickAgent.openActivityDurationTrack(false);

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getFamilyAndVillage();
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

    //首先请求用户所有家庭和小区
    //如果用户是未登录状态,那么直接去登录界面
    //如果用户是已登录状态,
    //可能1:新用户,没选择小区或者家庭,此时就去选择界面
    //可能2:老用户,选择了小区或者家庭,此时什么都不存,直接去首页
    //可能3:老用户,没有选择小区或者家庭(卸载之后重装的),此时获取列表,默认将第一项存到SP,作为默认初始数据
    //如果以上都不是,例如网络错误,那么就去登录界面,同时显示网络连接失败
    private void getFamilyAndVillage() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getFamilyAndVillage()
                        .subscribe(new Observer<UserFamiliesAndVillagesEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {
                                //新增了这个操作,所以这里会显示 功能登陆后可用  弹出的toast是在processNetworkResult中进行的
                                if (!processNetworkResult(userFamiliesAndVillagesEntity)) return;
                                if (NetHelper.CODE_NOT_LOGIN == userFamiliesAndVillagesEntity.getError_code()) {
                                    return;
                                }
                            }
                        }));
    }
}

