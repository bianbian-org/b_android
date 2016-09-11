package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.CheckInEntity;
import com.techjumper.polyhomeb.mvp.m.CheckInActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.CheckInActivity;

import java.util.List;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CheckInActivityPresenter extends AppBaseActivityPresenter<CheckInActivity> {

    private CheckInActivityModel mModel = new CheckInActivityModel(this);

    private Subscription mSubs1, mSubs2;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getChickInData();
    }

    @OnClick(R.id.iv_check_in)
    public void onClick(View view) {

        if (getView().mCanCheckIn) {
            checkIn();
        }
    }

    //获取签到数据
    private void getChickInData() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getCheckInData()
                        .subscribe(new Observer<CheckInEntity>() {
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
                            public void onNext(CheckInEntity checkInEntity) {
                                if (!processNetworkResult(checkInEntity)) return;
                                getView().processView(checkInEntity);
                            }
                        }));
    }

    private void checkIn() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.checkIn().subscribe(new Observer<CheckInEntity>() {
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
                    public void onNext(CheckInEntity checkInEntity) {
                        if (!processNetworkResult(checkInEntity)) return;
                        getView().processView(checkInEntity);
                    }
                })
        );
    }

    public List<DisplayBean> getData(CheckInEntity checkInEntity) {
        return mModel.getData(checkInEntity);
    }
}
