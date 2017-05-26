package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.RefreshPaymentEvent;
import com.techjumper.polyhomeb.mvp.p.activity.PaymentSuccessActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(PaymentSuccessActivityPresenter.class)
public class PaymentSuccessActivity extends AppBaseActivity<PaymentSuccessActivityPresenter> {

    @Bind(R.id.tv_pay_name)
    TextView mTvPayName;   //费用名称
    @Bind(R.id.tv_cost)
    TextView mTvCost;      //金额
    @Bind(R.id.tv_payment_way)
    TextView mTvPayWay;    //付款方式

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.payment_success);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvPayName.setText(getPresenter().getPayName());
        mTvCost.setText(getString(R.string.￥) + getPresenter().getTotal());
        mTvPayWay.setText(getPresenter().getPayWay());
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.payment_success);
    }

    @Override
    protected boolean onTitleLeftClick() {
        RxBus.INSTANCE.send(new RefreshPaymentEvent());
        new AcHelper.Builder(this).closeCurrent(true).target(PaymentActivity.class).start();
        return true;
    }
}
