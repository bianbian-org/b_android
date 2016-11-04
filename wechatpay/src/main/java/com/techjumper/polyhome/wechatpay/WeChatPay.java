package com.techjumper.polyhome.wechatpay;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WeChatPay {

    private IWXAPI api;
    private Activity mActivity;
    private String mAppId;
    private String mNoncestr;
    private String mPackageX;
    private String mPartnerid;
    private String mPrepayid;
    private String mSign;
    private String mTimestamp;

    public WeChatPay(Activity activity) {
        mActivity = activity;
        api = WXAPIFactory.createWXAPI(activity, Constants.APP_ID);
    }

    public void pay() {
        Toast.makeText(mActivity, "获取订单中...", Toast.LENGTH_SHORT).show();
        api.registerApp(mAppId);
        PayReq req = new PayReq();
        req.appId = mAppId;
        req.partnerId = mPartnerid;
        req.prepayId = mPrepayid;
        req.nonceStr = mNoncestr;
        req.timeStamp = mTimestamp;
        req.packageValue = mPackageX;
        req.sign = mSign;
        req.extData = "app data";
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    public void setOrderInfo(String appid, String noncestr, String packageX, String partnerid, String prepayid, String sign, String timestamp) {
        mAppId = appid;
        mNoncestr = noncestr;
        mPackageX = packageX;
        mPartnerid = partnerid;
        mPrepayid = prepayid;
        mSign = sign;
        mTimestamp = timestamp;
    }

    public boolean isOrderInfoLegal() {
        if (TextUtils.isEmpty(mAppId)
                || TextUtils.isEmpty(mNoncestr)
                || TextUtils.isEmpty(mPackageX)
                || TextUtils.isEmpty(mPartnerid)
                || TextUtils.isEmpty(mPrepayid)
                || TextUtils.isEmpty(mSign)
                || TextUtils.isEmpty(mTimestamp)) {
            return false;
        }
        return true;
    }
}
