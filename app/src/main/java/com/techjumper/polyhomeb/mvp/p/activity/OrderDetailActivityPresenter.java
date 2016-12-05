package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.m.OrderDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.OrderDetailActivity;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class OrderDetailActivityPresenter extends AppBaseActivityPresenter<OrderDetailActivity> {

    private OrderDetailActivityModel mModel = new OrderDetailActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        updateMessageState();
    }

    public String getObjId() {
        return mModel.getObjId();
    }

    private void updateMessageState() {
        if (mModel.getOrderId() == 0) return;
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.updateMessage()
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (trueEntity == null || trueEntity.getData() == null
                                        || TextUtils.isEmpty(trueEntity.getData().getResult())
                                        || !"true".equals(trueEntity.getData().getResult())) {
                                    return;
                                }

                            }
                        }));
    }
}
