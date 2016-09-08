package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.PaymentSuccessActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PaymentSuccessActivityModel extends BaseModel<PaymentSuccessActivityPresenter> {

    public PaymentSuccessActivityModel(PaymentSuccessActivityPresenter presenter) {
        super(presenter);
    }

    public Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getPayName() {
        return getExtra().getString(Constant.KEY_PAY_NAME);
    }

    public String getPayWay() {
        int way = getExtra().getInt(Constant.PAYMENT_WAY);
        switch (way) {
            case Constant.TENCENT_PAY:
                return getPresenter().getView().getString(R.string.tencent_pay);
            case Constant.ALIPAY:
                return getPresenter().getView().getString(R.string.alipay_pay);
            case Constant.UNION_PAY:
                return getPresenter().getView().getString(R.string.unionpay_pay);
            case Constant.YI_PAY:
                return getPresenter().getView().getString(R.string.yi_pay);
        }
        return "";
    }

    public double getTotal() {
        return getExtra().getDouble(Constant.KEY_PAY_ALL_COST);
    }
}
