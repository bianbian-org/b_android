package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ReloadMedicalMainEvent;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalAllUserEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalChangeLoginAccountActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeAccountActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeLoginAccountActivity;
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
public class MedicalChangeLoginAccountActivityPresenter extends AppBaseActivityPresenter<MedicalChangeLoginAccountActivity> {

    private MedicalChangeLoginAccountActivityModel mModel = new MedicalChangeLoginAccountActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick({R.id.layout_current_account, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                medicalLogin();
                break;
            case R.id.layout_current_account:
                new AcHelper.Builder(getView()).target(MedicalChangeAccountActivity.class).start();
                break;
        }
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
                                saveAllInfo2Sp(nztoken, result.response().body());
                                ToastUtils.show(getView().getString(R.string.success_login));
                                RxBus.INSTANCE.send(new ReloadMedicalMainEvent());
                                new AcHelper.Builder(getView())
                                        .closeCurrent(true)
                                        .target(MedicalMainActivity.class)
                                        .start();
                            }
                        })
        );

    }

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
