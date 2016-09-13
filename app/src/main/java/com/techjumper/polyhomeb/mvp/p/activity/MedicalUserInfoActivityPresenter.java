package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.MedicalUserInfoEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalUserInfoActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalUserInfoActivity;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalUserInfoActivityPresenter extends AppBaseActivityPresenter<MedicalUserInfoActivity> {

    private MedicalUserInfoActivityModel mModel = new MedicalUserInfoActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getUserInfo();
    }

    private void getUserInfo() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getInfo()
                        .subscribe(new Observer<MedicalUserInfoEntity>() {
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
                            public void onNext(MedicalUserInfoEntity entity) {
                                if (!processNetworkResult(entity)) return;
                                getView().onDataReceived(getData(entity));
                            }
                        })
        );
    }

    public List<DisplayBean> getData(MedicalUserInfoEntity entity) {
        return mModel.getData(entity);
    }
}
