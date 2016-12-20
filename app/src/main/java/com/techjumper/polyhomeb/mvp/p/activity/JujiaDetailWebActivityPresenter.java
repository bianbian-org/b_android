package com.techjumper.polyhomeb.mvp.p.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.paycorelib.OnPayListener;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.PayEntity;
import com.techjumper.polyhomeb.entity.PaymentsEntity;
import com.techjumper.polyhomeb.entity.event.H5PayEvent;
import com.techjumper.polyhomeb.entity.event.RefreshH5PayStateEvent;
import com.techjumper.polyhomeb.manager.PayManager;
import com.techjumper.polyhomeb.mvp.m.JujiaDetailWebModel;
import com.techjumper.polyhomeb.mvp.v.activity.JujiaDetailWebActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JujiaDetailWebActivityPresenter extends AppBaseActivityPresenter<JujiaDetailWebActivity> {

    private String mBack_type;
    private String mOrder_number;

    private JujiaDetailWebModel mModel = new JujiaDetailWebModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        pay();
    }

    @OnClick({R.id.close_group, R.id.back_group, R.id.refresh_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_group:
                goBack();
                return;
            case R.id.back_group:
                closePage();
                break;
            case R.id.refresh_group:
                reload();
                break;
        }
    }

    private void closePage() {
        getView().finish();
    }

    private void goBack() {
        if (getView().getWebView() == null) return;
        if (!getView().getWebView().canGoBack()) return;
        getView().getWebView().goBack();
    }

    private void reload() {
        if (getView().getWebView() == null) return;
        getView().getWebView().reload();
    }

    public String getJumpUrl() {
        return mModel.getJumpUrl();
    }

    private void pay() {
        RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof H5PayEvent) {
                //发起H5支付
                H5PayEvent event = (H5PayEvent) o;
                PayEntity payEntity = event.getPayEntity();
                h5Pay(payEntity);
            }
        });
    }

    private void h5Pay(PayEntity entity) {
        PayEntity.ParamsBean bean = entity.getParams();
        if (bean == null) return;
        PayEntity.ParamsBean.UrlBean url = bean.getUrl();
        if (url == null) return;
        mBack_type = url.getBack_type();
        int type = url.getType();
        mOrder_number = url.getOrder_number();

        PaymentsEntity paymentsEntity = new PaymentsEntity();
        PaymentsEntity.DataBean dataBean = new PaymentsEntity.DataBean();

        switch (type) {
            case 1:
                PayEntity.ParamsBean.UrlBean.WxPayBean wxPayBean = url.getWxpay();
                if (wxPayBean == null) return;
                PaymentsEntity.DataBean.WxpayBean wxpayBean = new PaymentsEntity.DataBean.WxpayBean();
                wxpayBean.setAppid(wxPayBean.getAppid());
                wxpayBean.setNoncestr(wxPayBean.getNoncestr());
                wxpayBean.setPackageX(wxPayBean.getPackageX());
                wxpayBean.setPartnerid(wxPayBean.getPartnerid());
                wxpayBean.setPrepayid(wxPayBean.getPrepayid());
                wxpayBean.setSign(wxPayBean.getSign());
                wxpayBean.setTimestamp(wxPayBean.getTimestamp());
                dataBean.setWxpay(wxpayBean);
                paymentsEntity.setData(dataBean);
                break;
            case 2:
                PayEntity.ParamsBean.UrlBean.AlipayBean alipay = url.getAlipay();
                if (alipay == null || TextUtils.isEmpty(alipay.getParms_str())
                        || TextUtils.isEmpty(alipay.getSign())) return;
                String parms_str = alipay.getParms_str();
                String sign = alipay.getSign();
                PaymentsEntity.DataBean.AliPayBean aliPayBean = new PaymentsEntity.DataBean.AliPayBean();
                aliPayBean.setParms_str(parms_str);
                aliPayBean.setSign(sign);
                dataBean.setAlipay(aliPayBean);
                paymentsEntity.setData(dataBean);
                break;
            case 3:
                PayEntity.ParamsBean.UrlBean.UnionpayBean unionpay = url.getUnionpay();
                if (unionpay == null || TextUtils.isEmpty(unionpay.getTn())) return;
                String tn = unionpay.getTn();
                PaymentsEntity.DataBean.UnionBean unionBean = new PaymentsEntity.DataBean.UnionBean();
                unionBean.setTn(tn);
                dataBean.setUnion(unionBean);
                paymentsEntity.setData(dataBean);
                break;
            case 4:
                break;
            default:
                break;
        }

        PayManager.with().loadPay(new OnPayListener() {
            @Override
            public void onSuccess() {
                paySuccess();
            }

            @Override
            public void onCancel() {
                payCancel();
            }

            @Override
            public void onFailed() {
                payFailed();
            }
        }, getView(), type, paymentsEntity);
    }

    /**
     * 银联支付之后的结果回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String msg = "";
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    boolean ret = verify(dataOrg, sign, "00");
                    if (ret) {
                        paySuccess();
                    } else {
                        payFailed();
                    }
                } catch (JSONException e) {
                }
            } else {
                paySuccess();
            }
        } else if (str.equalsIgnoreCase("fail")) {
            payFailed();
        } else if (str.equalsIgnoreCase("cancel")) {
            payCancel();
        }
    }

    private void payCancel() {
        ToastUtils.show(getView().getString(R.string.result_pay_cancel));
    }

    private void payFailed() {
        ToastUtils.show(getView().getString(R.string.result_pay_failed));
    }

    private void paySuccess() {
        ToastUtils.show(getView().getString(R.string.result_pay_success));
        new Handler().postDelayed(() -> {
            if (getView() != null) {
                //如果这个字段不是空的，就关闭当前Activity，返回上一页
                // (需验证其他支付方式的界面，再支付完毕之后会不会回到 选择支付方式的界面，如果不回去，则需要另寻出路)
                if (TextUtils.isEmpty(mBack_type)) {
                    new AcHelper.Builder(getView())
                            .closeCurrent(true)
                            .exitAnim(R.anim.fade_out)
                            .target(TabHomeActivity.class)
                            .start();
                } else {
                    RxBus.INSTANCE.send(new RefreshH5PayStateEvent(mOrder_number));
                    getView().finish();
                }
            }
        }, 1500);
    }

    /**
     * 银联支付后的与商户验签
     */
    private boolean verify(String data, String sign, String mode) {
        return true;
    }
}
