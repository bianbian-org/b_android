package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalLoginActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;
import com.techjumper.polyhomeb.user.UserManager;

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
                                ToastUtils.show(getView().getString(R.string.success_login));
                                new AcHelper.Builder(getView())
                                        .closeCurrent(true)
                                        .target(MedicalMainActivity.class)
                                        .start();
                            }
                        })
        );

    }
}
