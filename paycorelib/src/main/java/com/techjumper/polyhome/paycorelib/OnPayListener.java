package com.techjumper.polyhome.paycorelib;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public interface OnPayListener {
    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 支付取消
     */
    void onCancel();

    /**
     * 支付失败
     */
    void onFailed();
}
