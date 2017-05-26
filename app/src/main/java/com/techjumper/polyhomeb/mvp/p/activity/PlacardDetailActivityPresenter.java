package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.NoticeDetailEntity;
import com.techjumper.polyhomeb.mvp.m.PlacardDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.PlacardDetailActivity;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardDetailActivityPresenter extends AppBaseActivityPresenter<PlacardDetailActivity> {

    private PlacardDetailActivityModel mModel = new PlacardDetailActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        if (getComeFrom().equals("1")) {
            //从接口去取数据
            getDetail();
        } else if (getComeFrom().equals("2")) {
            //直接用
            getView().setUp(getContent());
        }
    }

    public String getType() {
        return mModel.getType();
    }

    public String getTime() {
        return mModel.getTime();
    }

    public String getTitle() {
        return mModel.getTitle();
    }

    public int getId() {
        return mModel.getId();
    }

    public String getContent() {
        return mModel.getContent();
    }

    public String getComeFrom() {
        return mModel.getComeFrom();
    }

    private void getDetail() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getDetail()
                        .subscribe(new Observer<NoticeDetailEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoticeDetailEntity noticeDetailEntity) {
                                if (!processNetworkResult(noticeDetailEntity)) return;
                                if (noticeDetailEntity == null || noticeDetailEntity.getData() == null)
                                    return;
                                getView().setUp(noticeDetailEntity.getData().getContent());
                            }
                        })
        );
    }
}
