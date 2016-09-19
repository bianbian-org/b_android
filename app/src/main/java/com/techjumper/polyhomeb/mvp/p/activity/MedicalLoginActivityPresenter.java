package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalLoginActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;

import butterknife.OnClick;
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
//        new AcHelper.Builder(getView()).target(MedicalMainActivity.class).start();
    }

    private void medicalLogin() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.medicalUserLogin(getView().getEtAccount().getEditableText().toString()
                        , getView().getEtPsw().getEditableText().toString())
                        .subscribe(new Observer<MedicalUserLoginEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(MedicalUserLoginEntity medicalUserLoginEntity) {
//                                if (processNetworkResult(medicalUserLoginEntity))
                                String s = medicalUserLoginEntity.toString();
                                JLog.e(s);
                            }
                        })
        );

    }
}
