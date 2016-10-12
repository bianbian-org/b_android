package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.alipay.AliPay;
import com.techjumper.polyhome.alipay.OnAliPayListener;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.PaymentsEntity;
import com.techjumper.polyhomeb.mvp.m.AdjustAccountsActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.AdjustAccountsActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PaymentSuccessActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdjustAccountsActivityPresenter extends AppBaseActivityPresenter<AdjustAccountsActivity>
        implements OnAliPayListener {

    private AdjustAccountsActivityModel mModel = new AdjustAccountsActivityModel(this);

    private Subscription mSubs1;

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
                getPaymentsInfoFromServer();
                break;
        }
    }

    private void getPaymentsInfoFromServer() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.payments(mCurrentPayment + "")
                        .subscribe(new Observer<PaymentsEntity>() {
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
                            public void onNext(PaymentsEntity paymentsEntity) {
                                getView().dismissLoading();
                                if (!processNetworkResult(paymentsEntity)) return;
                                loadPay(paymentsEntity);
                            }
                        }));
    }

    private void loadPay(PaymentsEntity paymentsEntity) {
        switch (mCurrentPayment) {
            case Constant.TENCENT_PAY:
                break;
            case Constant.ALIPAY:
                //if(支付宝的databean == null  || dataBean.parms_str()||databean.getSign()是空的字符串) break;
                String parms_str = paymentsEntity.getData().getParms_str();
                String sign = paymentsEntity.getData().getSign();
                String signs = "";
                try {
                    signs = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JLog.e(parms_str + "&sign=" + signs);
                AliPay aliPay = new AliPay(getView(), parms_str + "&sign=" + signs);
                aliPay.setListener(this);
                aliPay.pay();
                break;
            case Constant.UNION_PAY:
                break;
            case Constant.YI_PAY:
                break;
        }
    }

    @Override
    public void onSuccess() {
        ToastUtils.show("成功");
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.PAYMENT_WAY, mCurrentPayment);  //支付方式
        bundle.putString(Constant.KEY_PAY_NAME, getPayName()); //费用名称
        bundle.putDouble(Constant.KEY_PAY_ALL_COST, getTotal() + getExpiryPrice()); //总金额
        new AcHelper.Builder(getView()).target(PaymentSuccessActivity.class).extra(bundle).start();
    }

    @Override
    public void onCancel() {
        ToastUtils.show("取消");
    }

    @Override
    public void onWait() {
        ToastUtils.show("等待");
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
