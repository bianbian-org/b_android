package com.techjumper.polyhome.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.techjumper.polyhome.paycorelib.OnPayListener;

import java.util.Map;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AliPay {

    private static final int SDK_PAY_FLAG = 1;

    private OnPayListener mListener;
    private String mOrderInfo;

    public AliPay(String orderInfo) {
        mOrderInfo = orderInfo;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    //9000——订单支付成功
                    //8000——正在处理中
                    //4000——订单支付失败
                    //5000——重复请求
                    //6001——用户中途取消
                    //6002——网络连接出错
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (mListener != null) mListener.onSuccess();
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        if (mListener != null) mListener.onFailed();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        if (mListener != null) mListener.onCancel();
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        if (mListener != null) mListener.onFailed();
                    }
                    break;
            }
        }
    };

    /**
     * 支付
     */
    public void pay(final Activity activity) {
        try {
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(activity);
                    Map<String, String> result = alipay.payV2(mOrderInfo, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setListener(OnPayListener listener) {
        mListener = listener;
    }
}
