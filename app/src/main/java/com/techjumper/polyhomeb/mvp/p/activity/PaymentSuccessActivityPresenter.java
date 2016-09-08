package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.RefreshPaymentEvent;
import com.techjumper.polyhomeb.mvp.m.PaymentSuccessActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.PaymentActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PaymentSuccessActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PaymentSuccessActivityPresenter extends AppBaseActivityPresenter<PaymentSuccessActivity> {

    private PaymentSuccessActivityModel mModel = new PaymentSuccessActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_back)
    public void onClick(View view) {
        //发送RxBus到PaymentActivity,让他刷新 未缴费的  数据
        RxBus.INSTANCE.send(new RefreshPaymentEvent());
        new AcHelper.Builder(getView()).closeCurrent(true).target(PaymentActivity.class).start();
    }

    public String getPayName() {
        return mModel.getPayName();
    }

    public String getPayWay() {
        return mModel.getPayWay();
    }

    public double getTotal() {
        return mModel.getTotal();
    }
}
