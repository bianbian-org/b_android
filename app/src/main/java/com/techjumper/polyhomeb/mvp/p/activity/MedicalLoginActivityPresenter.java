package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ReloadMedicalMainEvent;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalAllUserEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalLoginActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalLoginActivityPresenter extends AppBaseActivityPresenter<MedicalLoginActivity> {

    private MedicalLoginActivityModel mModel = new MedicalLoginActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_login)
    public void onClick(View view) {
        medicalLogin();
    }

    private void medicalLogin() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.medicalUserLogin(getView().getEtAccount().getEditableText().toString()
                        , getView().getEtPsw().getEditableText().toString())
                        .subscribe(new Observer<Result<MedicalUserLoginEntity>>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                                JLog.e(e.getMessage());
                            }

                            @Override
                            public void onNext(Result<MedicalUserLoginEntity> result) {
                                if (result == null
                                        || result.response().body() == null
                                        || result.response().body().getStatus() != 1)
                                    return;
                                if (401 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_userid_pasw_wrong));
                                    return;
                                }
                                String nztoken = result.response().headers().get("nztoken");
                                if (TextUtils.isEmpty(nztoken)) return;
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN, nztoken);
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_ID, getView().getEtAccount().getEditableText().toString());
                                UserManager.INSTANCE.saveMedicalUserInfo(result.response().body());
                                RxBus.INSTANCE.send(new ReloadMedicalMainEvent());
                                saveAllInfo2Sp(nztoken, result.response().body());
                                ToastUtils.show(getView().getString(R.string.success_login));
                                new AcHelper.Builder(getView())
                                        .closeCurrent(true)
                                        .target(MedicalMainActivity.class)
                                        .start();
                            }
                        })
        );
    }

    // TODO: 2016/9/23  当修改了用户信息的时候,需要同步修改List中的用户信息

    // TODO: 2016/9/23  点了用户切换的话,帮他登录然后存下来,当前用户和List都要存,然后发消息到各个地方,刷新数据。另外退出当前这个账号。

    // TODO: 2016/9/23  日期格式化有问题

    // TODO: 2016/9/23  登录有时候会失败,,我再修改用户信息的时候,提示登录,然后网络连接失败

    // TODO: 2016/9/24  写个Demo页面看看RV的notifyItem(position)到底起不起作用,起作用的话,首页那10个就好说了,getView().getAdapter().notifyItemChanged(position).
    //如果第三方的RV不起作用,那就换成V7包的RV试下


    //将用户信息存入SP的List.
    private void saveAllInfo2Sp(String token, MedicalUserLoginEntity medicalUserLoginEntity) {

        List<MedicalAllUserEntity> userInfo = UserManager.INSTANCE.getMedicalAllUserInfo();

        boolean hasUserInfo = false;

        if (userInfo == null || userInfo.size() == 0) {
            userInfo = new ArrayList<>();
            MedicalAllUserEntity medicalAllUserEntity = new MedicalAllUserEntity();
            medicalAllUserEntity.setPassword(getView().getEtPsw().getEditableText().toString());
            medicalAllUserEntity.setId(medicalUserLoginEntity.getMember().getId());
            medicalAllUserEntity.setNickName(medicalUserLoginEntity.getMember().getNickname());
            medicalAllUserEntity.setpName(medicalUserLoginEntity.getMember().getPname());
            medicalAllUserEntity.setToken(token);
            userInfo.add(medicalAllUserEntity);
        } else {
            for (MedicalAllUserEntity entity : userInfo) {
                if (entity.getId().equals(medicalUserLoginEntity.getMember().getId())) {
                    hasUserInfo = true;
                    break;
                }
            }
        }
        //如果已经存在,那就更新信息
        if (hasUserInfo) {
            for (MedicalAllUserEntity entity : userInfo) {
                if (entity.getId().equals(medicalUserLoginEntity.getMember().getId())) {
                    entity.setpName(medicalUserLoginEntity.getMember().getPname());
                    entity.setPassword(getView().getEtPsw().getEditableText().toString());
                    entity.setToken(token);
                    entity.setNickName(medicalUserLoginEntity.getMember().getNickname());
                    break;
                }
            }
        } else { //不存在就存入集合
            MedicalAllUserEntity medicalAllUserEntity = new MedicalAllUserEntity();
            medicalAllUserEntity.setPassword(getView().getEtPsw().getEditableText().toString());
            medicalAllUserEntity.setId(medicalUserLoginEntity.getMember().getId());
            medicalAllUserEntity.setNickName(medicalUserLoginEntity.getMember().getNickname());
            medicalAllUserEntity.setpName(medicalUserLoginEntity.getMember().getPname());
            medicalAllUserEntity.setToken(token);
            userInfo.add(medicalAllUserEntity);
        }

        UserManager.INSTANCE.saveMedicalAllUserInfo(userInfo);
    }
}
