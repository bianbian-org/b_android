package com.techjumper.polyhomeb.other;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome.paycorelib.OnPayListener;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.JSH5PaymentsEntity;
import com.techjumper.polyhomeb.entity.JSJavaBaseEntity;
import com.techjumper.polyhomeb.entity.JSJavaContactShopEntity;
import com.techjumper.polyhomeb.entity.JSJavaImageViewEntity;
import com.techjumper.polyhomeb.entity.JSJavaNotificationEntity;
import com.techjumper.polyhomeb.entity.JSJavaPageJumpEntity;
import com.techjumper.polyhomeb.entity.PaymentsEntity;
import com.techjumper.polyhomeb.entity.event.WebViewNotificationEvent;
import com.techjumper.polyhomeb.manager.PayManager;
import com.techjumper.polyhomeb.mvp.p.activity.LoginActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyCommentActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.mvp.v.activity.WebViewShowBigPicActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JavascriptObject {

    private Activity mActivity;
    private String url = "";
    private long lastClickTime;

    public JavascriptObject(Activity mActivity) {
        this.mActivity = mActivity;
    }

    private boolean isFastDoubleClick(String url) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        boolean flag = this.url.equalsIgnoreCase(url);
        if (0 < timeD && timeD < 300 && flag) {
            this.url = url;
            return true;
        }
        this.url = url;
        lastClickTime = time;
        return false;
    }

    /**
     * 总的交互入口,根据json来判断执行的操作
     */
    @JavascriptInterface
    public void postMessage(String json) {
        JLog.e(json);
        if (TextUtils.isEmpty(json)) return;
        JSJavaBaseEntity baseEntity = GsonUtils.fromJson(json, JSJavaBaseEntity.class);
        if (baseEntity == null) return;
        String method = baseEntity.getMethod();
        if (TextUtils.isEmpty(method)) return;

        switch (method) {
            case "PageJump":
                JSJavaPageJumpEntity jsJavaPageJumpEntity = GsonUtils.fromJson(json, JSJavaPageJumpEntity.class);
                JSJavaPageJumpEntity.ParamsBean params = jsJavaPageJumpEntity.getParams();
                String url = params.getUrl();
                if (!isFastDoubleClick(url)) {
                    pageJump(url);
                }
                break;
            case "ImageView":
                JSJavaImageViewEntity jsJavaImageViewEntity = GsonUtils.fromJson(json, JSJavaImageViewEntity.class);
                JSJavaImageViewEntity.ParamsBean params1 = jsJavaImageViewEntity.getParams();
                int index = params1.getIndex();
                List<String> images = params1.getImages();
                String[] imageArray = new String[images.size()];
                for (int i = 0; i < images.size(); i++) {
                    imageArray[i] = images.get(i);
                }
                imageView(index, imageArray);
                break;
            case "RefreshLoaded":
                response();
                break;
            case "RefreshNotice":
                //2016/10/26  友邻网页做判断，如果是家庭权限就能点，如果不是就不能点(客户端做或者H5做都行.最好是客户端做)
                if (UserManager.INSTANCE.isFamily()) {
                    JSJavaNotificationEntity jsJavaNotificationEntity = GsonUtils.fromJson(json, JSJavaNotificationEntity.class);
                    JSJavaNotificationEntity.ParamsBean paramsBean = jsJavaNotificationEntity.getParams();
                    String result = paramsBean.getResult();
                    RxBus.INSTANCE.send(new WebViewNotificationEvent(result));
                }
                break;
            case "login":
                Bundle bundle = new Bundle();
                bundle.putString(LoginActivityPresenter.KEY_COME_FROM, LoginActivityPresenter.VALUE_COME_FROM_WEBVIEW);
                new AcHelper.Builder(mActivity).extra(bundle).closeCurrent(false).target(LoginActivity.class).start();
                break;
            case "pay":
                h5Pay(json);
                break;
            case "ContactShop":
                contactShop(json);
                break;

        }
    }

    /**
     * 跳转界面:回复评论,帖子详情
     */
    //两种情况,event对应事件,没有链接,http对应链接
    //event://NativeNewComment?id=11&token=123232
    //http://www.baidu.com?id=11&type=1
    //pay://order_num=11111&total=11.0
    private void pageJump(String url) {
        if (url.startsWith("http")) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.JS_PAGE_JUMP_URL, url);
            new AcHelper.Builder(mActivity).extra(bundle).target(ReplyDetailActivity.class).start();
        } else if (url.startsWith("event")) {
            //跳转回复帖子界面
            if (url.indexOf("NativeNewComment") > 0) {
                String article_id;
                String comment_id;
                String[] split = url.split("\\?");
                String params = split[1];  //id=11&token=123232
                String[] split1 = params.split("&");
                String s1 = split1[0];//id=11
                String s2 = split1[1];//type=dd
                String[] strings1 = s1.split("=");
                String[] strings2 = s2.split("=");
                //如果strings1的[0]字段和article_id相等,那么这个字段就是article_id字段
                if (Constant.JS_REPLY_ARTICLE_ID.equals(strings1[0])) {
                    if (strings1.length > 1) {
                        article_id = strings1[1];
                    } else {
                        article_id = "";
                    }
                    if (strings2.length > 1) {
                        comment_id = strings2[1];
                    } else {
                        comment_id = "";
                    }
                } else { //这个字段是comment_id
                    if (strings1.length > 1) {
                        article_id = strings2[1];
                    } else {
                        article_id = "";
                    }
                    if (strings2.length > 1) {
                        comment_id = strings1[1];
                    } else {
                        comment_id = "";
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.JS_REPLY_ARTICLE_ID, article_id);
                bundle.putString(Constant.JS_REPLY_COMMENT_ID, comment_id);
                new AcHelper.Builder(mActivity).extra(bundle).target(ReplyCommentActivity.class).start();
            }
        }
    }

    /**
     * 查看大图
     */
    private void imageView(int index, String[] urls) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.JS_BIG_PIC_INDEX, index);
        bundle.putStringArray(Constant.JS_BIG_PIC_ARRAY, urls);
        new AcHelper.Builder(mActivity).extra(bundle).target(WebViewShowBigPicActivity.class).start();
    }

    /**
     * 刷新
     */
    private void response() {
//        RxBus.INSTANCE.send(new RefreshStopEvent());
    }

    private void h5Pay(String json) {
        JSJavaPageJumpEntity payEntity = GsonUtils.fromJson(json, JSJavaPageJumpEntity.class);
        JSJavaPageJumpEntity.ParamsBean payParams = payEntity.getParams();
        String payJson = payParams.getUrl();
        if (TextUtils.isEmpty(payJson)) return;
        JSH5PaymentsEntity paymentsEntity = GsonUtils.fromJson(payJson, JSH5PaymentsEntity.class);
        if (paymentsEntity.getType() == -1) return;
        int type = paymentsEntity.getType();

        PaymentsEntity entity = new PaymentsEntity();
        PaymentsEntity.DataBean dataBean = new PaymentsEntity.DataBean();

        switch (type) {
            case 1:
                break;
            case 2:
                JSH5PaymentsEntity.AlipayBean alipay = paymentsEntity.getAlipay();
                if (alipay == null
                        || TextUtils.isEmpty(alipay.getParms_str())
                        || TextUtils.isEmpty(alipay.getSign())) return;
                String parms_str = alipay.getParms_str();
                String sign = alipay.getSign();
                PaymentsEntity.DataBean.AliPayBean aliPayBean = new PaymentsEntity.DataBean.AliPayBean();
                aliPayBean.setParms_str(parms_str);
                aliPayBean.setSign(sign);
                dataBean.setAlipay(aliPayBean);
                entity.setData(dataBean);
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
        PayManager.with().loadPay(new OnPayListener() {
            @Override
            public void onSuccess() {
                ToastUtils.show(mActivity.getString(R.string.result_pay_success));
                new Handler().postDelayed(() -> {
                    if (mActivity != null) {
                        new AcHelper.Builder(mActivity)
                                .closeCurrent(true)
                                .exitAnim(R.anim.fade_out)
                                .target(TabHomeActivity.class)
                                .start();
                    }
                }, 1500);
            }

            @Override
            public void onCancel() {
                ToastUtils.show(mActivity.getString(R.string.result_pay_cancel));
            }

            @Override
            public void onWait() {
                ToastUtils.show(mActivity.getString(R.string.result_pay_wait));
            }

            @Override
            public void onFailed() {
                ToastUtils.show(mActivity.getString(R.string.result_pay_failed));
            }
        }, mActivity, type, entity);
    }

    private void contactShop(String json) {
        JSJavaContactShopEntity jsJavaContactShopEntity = GsonUtils.fromJson(json, JSJavaContactShopEntity.class);
        if (jsJavaContactShopEntity == null
                && jsJavaContactShopEntity.getParams() == null) return;
        JSJavaContactShopEntity.ParamsBean paramsBean = jsJavaContactShopEntity.getParams();
        String tel = paramsBean.getTel();
        String shop_id = paramsBean.getShop_id();

        if (!TextUtils.isEmpty(tel)) {
            RxPermissions.getInstance(mActivity)
                    .request(Manifest.permission.CALL_PHONE)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            DialogUtils.getBuilder(mActivity)
                                    .content(String.format(mActivity.getString(R.string.confirm_call_x), tel))
                                    .positiveText(R.string.ok)
                                    .negativeText(R.string.cancel)
                                    .onAny((dialog, which) -> {
                                        switch (which) {
                                            case POSITIVE:
                                                Intent intent = new Intent();
                                                intent.setAction(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse(tel));
                                                mActivity.startActivity(intent);

                                                // TODO: 2016/11/1  调用接口，通知商铺id
                                                break;
                                        }
                                    })
                                    .show();
                        }
                    });
        }

    }

}