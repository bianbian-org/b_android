package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.AdjustAccountsActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(AdjustAccountsActivityPresenter.class)
public class AdjustAccountsActivity extends AppBaseActivity<AdjustAccountsActivityPresenter> {

    @Bind(R.id.tv_pay_name)
    TextView mTvPayName;       //费用名称
    @Bind(R.id.tv_pay_type)
    TextView mTvPayType;       //费用类型
    @Bind(R.id.tv_pay_object)
    TextView mTvPayObj;        //收费对象
    @Bind(R.id.tv_pay_death_line)
    TextView mTvDeathLine;     //截止日期
    @Bind(R.id.tv_total)
    TextView mTvNotTotal;      //总价(不包括滞纳金)
    @Bind(R.id.tv_other_cost)
    TextView mTvOther;         //逾期X天,滞纳金XX
    @Bind(R.id.tv_total_with_all)
    TextView mTvPayAll;        //总价(包括滞纳金)
    @Bind(R.id.tv_pay)
    TextView mTvPay;           //付款
    @Bind(R.id.layout_tencent_pay)
    RelativeLayout mLayoutTencentPay;  //微信支付布局
    @Bind(R.id.iv_tencent_pay)
    ImageView mIvTencentPay;           //微信支付打钩
    @Bind(R.id.layout_alipay)
    RelativeLayout mLayoutAliPay;      //支付宝支付布局
    @Bind(R.id.iv_alipay)
    ImageView mIvAliPay;               //支付宝支付打钩
    @Bind(R.id.layout_union_pay)
    RelativeLayout mLayoutUnionPay;  //银联支付布局
    @Bind(R.id.iv_union_pay)
    ImageView mIvUnionPay;           //一银联支付打钩
    @Bind(R.id.layout_yi_pay)
    RelativeLayout mLayoutYiPay;  //翼支付布局
    @Bind(R.id.iv_yi_pay)
    ImageView mIvYiPay;           //翼支付打钩

    private static final int sLate = 1; //已经逾期
    private static final int sNotLate = 0;  //未逾期

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_adjust_accounts);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setDataToView();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.adjust_accounts);
    }

    private void setDataToView() {
        mTvPayName.setText(getPresenter().getPayName());  //费用名称
        mTvPayType.setText(getPresenter().getPayTypeString()); //费用类型
        mTvPayObj.setText(getPresenter().getPayObj());    //收费对象
        mTvDeathLine.setText(getPresenter().getDeathLine()); //截止日期
        mTvNotTotal.setText(getString(R.string.￥) + getPresenter().getTotal()); //总价,不包含滞纳金
        if (sLate == getPresenter().getIsLate()) {   //已经逾期
            String text1 = String.format(getString(R.string.late_x_day), Math.abs(getPresenter().getDay())); //超过X天
            String text2 = getString(R.string.x_exceed);  //产生
            String text3 = getString(R.string.yuan);  //元
            String text4 = getString(R.string.expiry_price);  //滞纳金
            mTvOther.setText(Html.fromHtml(text1 + text2 + "<font color='#ff7f00'>"
                    + getPresenter().getExpiryPrice() + text3 + "</font>" + text4));
        } else if (sNotLate == getPresenter().getIsLate()) {  //未逾期
            mTvOther.setText(String.format(getString(R.string.surplus_day), getPresenter().getDay()));
        }
        mTvPayAll.setText(getPresenter().getTotal() + getPresenter().getExpiryPrice() + "");  //合计
    }

    public ImageView getIvTencentPay() {
        return mIvTencentPay;
    }

    public ImageView getIvAliPay() {
        return mIvAliPay;
    }

    public ImageView getIvUnionPay() {
        return mIvUnionPay;
    }

    public ImageView getIvYiPay() {
        return mIvYiPay;
    }
}
