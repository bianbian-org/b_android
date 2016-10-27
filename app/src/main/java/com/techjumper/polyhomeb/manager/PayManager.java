package com.techjumper.polyhomeb.manager;

import android.app.Activity;
import android.text.TextUtils;

import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.alipay.AliPay;
import com.techjumper.polyhome.paycorelib.OnPayListener;
import com.techjumper.polyhome.wechatpay.WeChatPay;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.PaymentsEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PayManager {

    private static PayManager sPayManager;
    private OnPayListener onPayListener;
    private Activity context;
    private AliPay aliPay;
    private WeChatPay weChatPay;

    private PayManager() {
    }

    public static PayManager with() {
        if (sPayManager == null) {
            sPayManager = new PayManager();
        }
        return sPayManager;
    }

    public void loadPay(OnPayListener onPayListener, Activity context,
                        int mCurrentPayment, PaymentsEntity paymentsEntity) {
        this.onPayListener = onPayListener;
        this.context = context;
        switch (mCurrentPayment) {
            case Constant.TENCENT_PAY:
                weChatPay(paymentsEntity);
                break;
            case Constant.ALIPAY:
                aliPay(paymentsEntity);
                break;
            case Constant.UNION_PAY:
                break;
            case Constant.YI_PAY:
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay(PaymentsEntity paymentsEntity) {
        if (paymentsEntity.getData().getAlipay() == null
                || TextUtils.isEmpty(paymentsEntity.getData().getAlipay().getParms_str())
                || TextUtils.isEmpty(paymentsEntity.getData().getAlipay().getSign())) {
            ToastUtils.show(context.getString(R.string.pay_order_info));
            return;
        }
        String parms_str = paymentsEntity.getData().getAlipay().getParms_str();
        String sign = paymentsEntity.getData().getAlipay().getSign();
        String signs = "";
        try {
            signs = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        aliPay = new AliPay(context, parms_str + "&sign=" + signs);
        if (onPayListener != null) {
            aliPay.setListener(onPayListener);
        }
        aliPay.pay();
    }

    /**
     * 微信支付
     */
    private void weChatPay(PaymentsEntity paymentsEntity) {
        weChatPay = new WeChatPay(context);
        if (paymentsEntity.getData().getWxpay() == null) {
            ToastUtils.show(context.getString(R.string.pay_order_info));
            return;
        }
        PaymentsEntity.DataBean.WxpayBean bean = paymentsEntity.getData().getWxpay();
        weChatPay.setOrderInfo(bean.getAppid(), bean.getNoncestr(), bean.getPackageX(), bean.getPartnerid()
                , bean.getPrepayid(), bean.getSign(), bean.getTimestamp());
        if (!weChatPay.isOrderInfoLegal()) {
            ToastUtils.show(context.getString(R.string.pay_order_info));
            return;
        }
        if (onPayListener != null) {
            weChatPay.setListener(onPayListener);
        }
        weChatPay.pay();
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (onPayListener != null) {
            if (aliPay != null) {
                aliPay.setListener(null);
            }
        }
    }
}