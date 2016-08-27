package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.mvp.m.MyVillageFamilyActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChooseVillageFamilyActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MyVillageFamilyActivity;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MyVillageFamilyActivityPresenter extends AppBaseActivityPresenter<MyVillageFamilyActivity> {

    private MyVillageFamilyActivityModel mModel = new MyVillageFamilyActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getFamilyAndVillage();
    }

    public void onTitleRightClick() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_COME_FROM, Constant.VALUE_COME_FROM);
        new AcHelper.Builder(getView()).extra(bundle).target(ChooseVillageFamilyActivity.class).start();
    }

    private void getFamilyAndVillage() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getFamilyAndVillage()
                        .subscribe(new Observer<UserFamiliesAndVillagesEntity>() {
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
                            public void onNext(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {
                                getView().dismissLoading();
                                if (!processNetworkResult(userFamiliesAndVillagesEntity)) return;
                                if (userFamiliesAndVillagesEntity.getData() != null) {
                                    mModel.processData(userFamiliesAndVillagesEntity);
                                }
                            }
                        }));
    }
}
