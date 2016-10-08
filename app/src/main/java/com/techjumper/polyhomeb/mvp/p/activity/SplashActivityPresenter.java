package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;

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
    }

    //首先请求用户所有家庭和小区
    //如果用户是未登录状态,那么直接去登录界面(意思是说此用户都没登录)  已做处理
    //如果用户是已登录状态,
    //可能1:新用户,没选择小区或者家庭,此时就去选择界面(意思是说此用户是新用户,或者说此用户没有家庭并且没有小区)  已做处理
    //可能2:老用户,选择了小区或者家庭,此时什么都不存,直接去首页(意思是说此用户是老用户,只是重新打开app而已)     已做处理
    //可能3:老用户,没有选择小区或者家庭(卸载之后重装的),此时获取列表,默认将第一项存到SP,作为默认初始数据(意思是说此用户虽然之前选择过家庭或者小区,但是他卸载了app,所以此时取出家庭第一项存起来作为默认,如果家庭没有数据,则取出小区的第一项)   已做处理
    //如果以上都不是,例如网络错误,那么就去登录界面,同时显示网络连接失败(这项有待商榷)  已做处理,跳转至登录界面
    public void getFamilyAndVillage() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getFamilyAndVillage()
                        .subscribe(new Observer<UserFamiliesAndVillagesEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                //此处本身是jumpToTabHomeActivity的,但是如果走到error了,
                                //就会导致在splash页面停留过久,并且跳转到jumpToTabHomeActivity,
                                //如果此时是新用户之类的,跳转过去,就会出问题,因为小区和家庭都没得。
                                //所以这时候直接跳转到登录界面去,(在登录里面会清空sp数据,做到真正的退出登录了,再重新登录)
//                                jumpToTabHomeActivity();
                                getView().showError(e);
                                jumpToLoginActivity();
                            }

                            @Override
                            public void onNext(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {
                                if (NetHelper.CODE_NOT_LOGIN == userFamiliesAndVillagesEntity.getError_code()) {
                                    //新增了这个操作,所以这里会显示 功能登陆后可用  弹出的toast是在processNetworkResult中进行的.只为了屏蔽Toast
                                    jumpToLoginActivity();
                                    return;
                                }
                                //(userFamiliesAndVillagesEntity是空的)短路与((家庭是空的或者大小为0)并上(小区是空的或者大小为0))
                                // 说明是新用户.也就是说此用户虽然登录了,但是没有选择家庭或者小区就退出app了.
                                // 同时也说明此用户没有加入过任何家庭或者小区,所以可断定为注册之后什么都没做的新用户.
                                if (userFamiliesAndVillagesEntity.getData() == null
                                        || ((userFamiliesAndVillagesEntity.getData().getFamily_infos() == null
                                        || userFamiliesAndVillagesEntity.getData().getFamily_infos().size() == 0)
                                        && (userFamiliesAndVillagesEntity.getData().getVillage_infos() == null
                                        || userFamiliesAndVillagesEntity.getData().getVillage_infos().size() == 0))) {
                                    jumpToChooseFamilyAndVillageActivity();
                                    return;
                                }
                                //如果userFamiliesAndVillagesEntity不是空的,并且里面的家庭或者小区不是空的,并且SP中有相应的数据
                                //那么直接跳转首页
                                if (userFamiliesAndVillagesEntity.getData() != null
                                        && ((userFamiliesAndVillagesEntity.getData().getFamily_infos() != null
                                        && userFamiliesAndVillagesEntity.getData().getFamily_infos().size() != 0)
                                        || (userFamiliesAndVillagesEntity.getData().getVillage_infos() != null
                                        && userFamiliesAndVillagesEntity.getData().getVillage_infos().size() != 0))
                                        && !TextUtils.isEmpty(UserManager.INSTANCE.getCurrentTitle())) {
                                    jumpToTabHomeActivity();
                                    return;
                                }
                                //和上面差不多,只是SP中没有数据,即:
                                //userFamiliesAndVillagesEntity不是空的,并且里面的家庭或者小区不是空的,但是SP中没有相应的数据
                                //那么->如果家庭不是空的,就取出家庭第一项作为默认值,存入SP,并且跳转首页.如果家庭是空的,则取出小区第一项存入SP,并跳转首页
                                if (userFamiliesAndVillagesEntity.getData() != null
                                        && ((userFamiliesAndVillagesEntity.getData().getFamily_infos() != null
                                        && userFamiliesAndVillagesEntity.getData().getFamily_infos().size() != 0)
                                        || (userFamiliesAndVillagesEntity.getData().getVillage_infos() != null
                                        && userFamiliesAndVillagesEntity.getData().getVillage_infos().size() != 0))
                                        && TextUtils.isEmpty(UserManager.INSTANCE.getCurrentTitle())) {
                                    saveDataToSp(userFamiliesAndVillagesEntity);
                                    jumpToTabHomeActivity();
                                    return;
                                }
                            }
                        }));
    }

    private void jumpToLoginActivity() {
        new AcHelper.Builder(getView())
                .target(LoginActivity.class)
                .closeCurrent(true)
                .enterAnim(R.anim.fade_in)
                .exitAnim(R.anim.fade_out)
                .start();
        getView().setCanBack(true);
    }

    private void jumpToChooseFamilyAndVillageActivity() {
        new AcHelper.Builder(getView())
                .target(ChooseVillageFamilyActivity.class)
                .closeCurrent(true)
                .enterAnim(R.anim.fade_in)
                .exitAnim(R.anim.fade_out)
                .start();
        getView().setCanBack(true);
    }

    private void jumpToTabHomeActivity() {
        new AcHelper.Builder(getView())
                .target(TabHomeActivity.class)
                .closeCurrent(true)
                .enterAnim(R.anim.fade_in)
                .exitAnim(R.anim.fade_out)
                .start();
        getView().setCanBack(true);
    }

    private void saveDataToSp(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {
        mModel.saveDataToSp(userFamiliesAndVillagesEntity);
    }
}

