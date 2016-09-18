package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.MedicalMainEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalMainActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeLoginAccountActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalUserInfoActivity;

import java.util.List;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainActivityPresenter extends AppBaseActivityPresenter<MedicalMainActivity> {

    private MedicalMainActivityModel mModel = new MedicalMainActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getMedicalData();
    }

    public void onTitleRightClick() {
        new AcHelper.Builder(getView()).target(MedicalChangeLoginAccountActivity.class).start();
    }

    @OnClick(R.id.layout_user_info)
    public void onClick(View view) {
        new AcHelper.Builder(getView()).target(MedicalUserInfoActivity.class).start();
    }

    private void getMedicalData() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getMedicalData().subscribe(new Observer<MedicalMainEntity>() {
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
                    public void onNext(MedicalMainEntity medicalMainEntity) {
                        if (!processNetworkResult(medicalMainEntity)) return;
                        getView().onDataReceived(mModel.getData(medicalMainEntity));
                    }
                }));
    }

    public List<DisplayBean> getData(MedicalMainEntity entity) {
        return mModel.getData(entity);
    }
}
