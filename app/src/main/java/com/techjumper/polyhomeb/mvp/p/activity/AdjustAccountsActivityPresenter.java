package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.AdjustAccountsActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.AdjustAccountsActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PaymentSuccessActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdjustAccountsActivityPresenter extends AppBaseActivityPresenter<AdjustAccountsActivity> {

    private AdjustAccountsActivityModel mModel = new AdjustAccountsActivityModel(this);

    public int mCurrentPayment = 0;  //选择的付款方式
    public List<ImageView> mPaymentWay = new ArrayList<>();

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        initPayment();
    }

    private void initPayment() {
        mPaymentWay.clear();
        mPaymentWay.add(getView().getIvTencentPay());
        mPaymentWay.add(getView().getIvAliPay());
        mPaymentWay.add(getView().getIvUnionPay());
        mPaymentWay.add(getView().getIvYiPay());
        processStatus(Constant.TENCENT_PAY);
    }

    //处理选择支付方式和打钩状态
    private void processStatus(int which) {
        for (int i = 0; i < mPaymentWay.size(); i++) {
            if (i == which) {
                PicassoHelper.getDefault().load(R.mipmap.icon_choose_green).into(mPaymentWay.get(i));
            } else {
                mPaymentWay.get(i).setImageBitmap(null);
            }
        }
        mCurrentPayment = which;
    }

    @OnClick({R.id.layout_tencent_pay, R.id.layout_alipay, R.id.layout_union_pay, R.id.layout_yi_pay, R.id.tv_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_tencent_pay:
                processStatus(Constant.TENCENT_PAY);
                break;
            case R.id.layout_alipay:
                processStatus(Constant.ALIPAY);
                break;
            case R.id.layout_union_pay:
                processStatus(Constant.UNION_PAY);
                break;
            case R.id.layout_yi_pay:
                processStatus(Constant.YI_PAY);
                break;
            case R.id.tv_pay:
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.PAYMENT_WAY, mCurrentPayment);  //支付方式
                bundle.putString(Constant.KEY_PAY_NAME, getPayName()); //费用名称
                // TODO: 16/9/8  此处的总金额要以支付平台回调为准
                bundle.putDouble(Constant.KEY_PAY_ALL_COST, getTotal() + getExpiryPrice()); //总金额
                new AcHelper.Builder(getView()).target(PaymentSuccessActivity.class).extra(bundle).start();
                break;

            // TODO: 16/9/8  支付失败之后,在此界面弹框,告知支付失败的原因,点击确定什么的,可以重新付款
        }
    }

    /**
     * 订单号 334209320948023
     */
    public String getOrderNum() {
        return mModel.getOrderNum();
    }

    /**
     * 费用名称  2月水费
     */
    public String getPayName() {
        return mModel.getPayName();
    }

    /**
     * 收费对象  2栋2楼202
     */
    public String getPayObj() {
        return mModel.getPayObj();
    }

    /**
     * 截止日期 2016-6-6
     */
    public String getDeathLine() {
        return mModel.getDeathLine();
    }

    /**
     * 总价(不包括滞纳金)
     */
    public double getTotal() {
        return mModel.getTotal();
    }

    /**
     * 滞纳金
     */
    public double getExpiryPrice() {
        return mModel.getExpiryPrice();
    }

    /**
     * 费用类型  1-物业费 2-水费 3-电费 4-燃气费 5-其他
     */
    public int getPayType() {
        return mModel.getPayType();
    }

    /**
     * 费用类型  1-物业费 2-水费 3-电费 4-燃气费 5-其他
     */
    public String getPayTypeString() {
        return mModel.getPayTypeString();
    }

    /**
     * //是否逾期 0-没逾期, 1-逾期
     */
    public int getIsLate() {
        return mModel.getIsLate();
    }

    /**
     * 超过X天,还剩下10天,是+10天,逾期超过了10天则是-10天
     */
    public int getDay() {
        return mModel.getDay();
    }
}
