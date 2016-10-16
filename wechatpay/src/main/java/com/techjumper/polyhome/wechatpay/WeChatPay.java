package com.techjumper.polyhome.wechatpay;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.techjumper.polyhome.paycorelib.OnPayListener;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class WeChatPay implements IWXAPIEventHandler {

    private IWXAPI api;
    private Activity mActivity;
    private OnPayListener mListener;

    private String mAppId;
    private String mNoncestr;
    private String mPackageX;
    private String mPartnerid;
    private String mPrepayid;
    private String mSign;
    private int mTimestamp = -1;

    public WeChatPay(Activity activity) {
        mActivity = activity;
        api = WXAPIFactory.createWXAPI(activity, "wxb4ba3c02aa476ea1");
    }

    public void pay() {
        Toast.makeText(mActivity, "获取订单中...", Toast.LENGTH_SHORT).show();
        PayReq req = new PayReq();
        req.appId = "wxb4ba3c02aa476ea1";
        req.partnerId = "1305176001";
        req.prepayId = "wx2016101314565885172f424c0678868797";
        req.nonceStr = "dc73dc84944447f0f14704dc6a77d036";
        req.timeStamp = System.currentTimeMillis() + "";
        req.packageValue = "Sign=WXPay";
        req.sign = "6D7DFCF3272C9E7157A11C7C78A71F5A";
        req.extData = "app data";
        Toast.makeText(mActivity, "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    public void setListener(OnPayListener listener) {
        mListener = listener;
    }

    public void setOrderInfo(String appid, String noncestr, String packageX, String partnerid, String prepayid, String sign, int timestamp) {
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
                || mTimestamp == -1) {
            return false;
        }
        return true;
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                if (mListener != null) {
                    mListener.onSuccess();
                }
            } else if (resp.errCode == -1) {
                //微信支付 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                if (mListener != null) {
                    mListener.onFailed();
                }
            } else if (resp.errCode == -2) {
                //微信支付 用户取消
                if (mListener != null) {
                    mListener.onCancel();
                }
            }
        }
    }
}
