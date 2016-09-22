package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalChangeAccountEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalChangeAccountActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeAccountActivity;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalChangeAccountActivityPresenter extends AppBaseActivityPresenter<MedicalChangeAccountActivity> {

    private MedicalChangeAccountActivityModel mModel = new MedicalChangeAccountActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getData();
    }

    private void getData() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getMedicalUserListData()
                        .subscribe(new Observer<MedicalChangeAccountEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                                getView().onDataReceived(mModel.getEmptyData());
                            }

                            @Override
                            public void onNext(MedicalChangeAccountEntity medicalChangeAccountEntity) {
                                if (!processNetworkResult(medicalChangeAccountEntity)) {
                                    getView().onDataReceived(mModel.getEmptyData());
                                } else {
                                    getView().dismissLoading();
                                    getView().onDataReceived(mModel.getData(medicalChangeAccountEntity));
                                }
                            }
                        }));
    }
}
