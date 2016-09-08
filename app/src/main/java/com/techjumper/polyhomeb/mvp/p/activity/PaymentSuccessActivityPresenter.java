package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.PaymentSuccessActivityModel;
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
        getView().onBackPressed();
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
