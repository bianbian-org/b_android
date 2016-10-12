package com.techjumper.polyhome.alipay;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public interface OnAliPayListener {
    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 支付取消
     */
    void onCancel();

    /**
     * 等待确认
     */
    void onWait();
}
