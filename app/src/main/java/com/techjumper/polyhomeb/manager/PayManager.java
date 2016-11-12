package com.techjumper.polyhomeb.manager;

import android.app.Activity;
import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.alipay.AliPay;
import com.techjumper.polyhome.paycorelib.OnPayListener;
import com.techjumper.polyhome.wechatpay.WeChatPay;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.PaymentsEntity;
import com.techjumper.polyhomeb.entity.event.WechatPayResultEvent;
import com.unionpay.UPPayAssistEx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.Subscription;

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

    private Subscription mSubs1;

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
                unionPay(paymentsEntity);
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
        if (paymentsEntity.getData().getWxpay() == null
                || TextUtils.isEmpty(paymentsEntity.getData().getWxpay().getAppid())) {
            ToastUtils.show(context.getString(R.string.pay_order_info));
            return;
        }
        PaymentsEntity.DataBean.WxpayBean bean = paymentsEntity.getData().getWxpay();
        weChatPay = new WeChatPay(context);
        weChatPay.setOrderInfo(bean.getAppid(), bean.getNoncestr(), bean.getPackageX(), bean.getPartnerid()
                , bean.getPrepayid(), bean.getSign(), bean.getTimestamp());
        if (!weChatPay.isOrderInfoLegal()) {
            ToastUtils.show(context.getString(R.string.pay_order_info));
            return;
        }
        registWechatPayResult(onPayListener);
        weChatPay.pay();
    }

    private void registWechatPayResult(OnPayListener onPayListener) {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        mSubs1 = RxBus.INSTANCE
                .asObservable()
                .subscribe(o -> {
                    if (o instanceof WechatPayResultEvent) {
                        WechatPayResultEvent event = (WechatPayResultEvent) o;
                        int result = event.getResult();
                        if (onPayListener != null) {
                            switch (result) {
                                case 0:
                                    onPayListener.onSuccess();
                                    break;
                                case -1:
                                    onPayListener.onFailed();
                                    break;
                                case -2:
                                    onPayListener.onCancel();
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * 银联支付
     */
    private void unionPay(PaymentsEntity paymentsEntity) {
        if (paymentsEntity.getData().getUnion() == null) {
            ToastUtils.show(context.getString(R.string.pay_order_info));
            return;
        }
        String tn = paymentsEntity.getData().getUnion().getTn();
//        String mode = paymentsEntity.getData().getUnion().getMode();
        UPPayAssistEx.startPay(context, null, null, tn, "01");
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        if (onPayListener != null) {
            if (aliPay != null) {
                aliPay.setListener(null);
            }
        }
    }
}