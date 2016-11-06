package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.paycorelib.OnPayListener;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.PayEntity;
import com.techjumper.polyhomeb.entity.PaymentsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.event.H5PayEvent;
import com.techjumper.polyhomeb.entity.event.JSArticleIdEvent;
import com.techjumper.polyhomeb.entity.event.JSCallPhoneNumberEvent;
import com.techjumper.polyhomeb.entity.event.RefreshH5PayStateEvent;
import com.techjumper.polyhomeb.entity.event.RefreshWhenDeleteArticleEvent;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.manager.PayManager;
import com.techjumper.polyhomeb.mvp.m.JSInteractionActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.JSInteractionActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JSInteractionActivityPresenter extends AppBaseActivityPresenter<JSInteractionActivity> {

    private JSInteractionActivityModel mModel = new JSInteractionActivityModel(this);

    private Subscription mSubs1, mSubs2, mSubs3;
    private String mArticleId;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable()
                        .subscribe(o -> {
                            if (o instanceof ReloadWebPageEvent) {
                                if (getView().webViewIsInit()) {
                                    getView().getWebView().reload();
                                }
                            } else if (o instanceof JSCallPhoneNumberEvent) {
                                JSCallPhoneNumberEvent event = (JSCallPhoneNumberEvent) o;
                                //比对当前Activity和event传过来的hashCode，必须相同才进入下一步，否则return
                                //因为JSInteractionActivity启动模式是standard，开启多少就有多少个实例在Activity栈
                                //所以当栈中已经存在数个JSInteractionActivity的时候，JavascriptObject中再发消息，
                                //所有的JSInteractionActivity都会收到那个消息，那么就都会走下面的步骤，也就都会弹出对话框
                                //造成的问题就是，当前界面弹出对话框，关闭当前界面之后，露出来的JSInteractionActivity会显示一个对话框
                                //所以此处比对哈希值，如若发现哈希值不同，证明是JavascriptObject发出消息之后，
                                //JSInteractionActivity1和JSInteractionActivity2以及...JSInteractionActivityN都收到消息了
                                //但是由于哈希值不同，所以能分辨
                                //我在JSInteractionActivity1中点击了拨打电话按钮，那么就只应该在JSInteractionActivity1上创建对话框，
                                //其他JSInteractionActivity2，JSInteractionActivity3，JSInteractionActivityN哈希值不同，
                                //即使收到了消息，也不能创建对话框，所以哈希值相同的情况下，能证明我在JSInteractionActivity1中的JavascriptObject
                                //发消息出来，JSInteractionActivity1收到了，就弹出对话框.
                                if (event.getHashCode() != getView().hashCode()) return;
                                showCallNumDialog(event);
                            } else if (o instanceof JSArticleIdEvent) {
                                //需要删除的文章的id
                                JSArticleIdEvent event = (JSArticleIdEvent) o;
                                mArticleId = event.getId();
                            } else if (o instanceof H5PayEvent) {
                                //发起H5支付
                                H5PayEvent event = (H5PayEvent) o;
                                int hashCode = event.getHashCode();
                                if (hashCode != getView().hashCode()) return;
                                PayEntity payEntity = event.getPayEntity();
                                h5Pay(payEntity);
                            } else if (o instanceof RefreshH5PayStateEvent) {
                                //当商城订单支付的时候，有多个商铺的订单，此时支付了其中一个，那么就要刷新
                                //具体就是客户端调用H5的JS方法，将订单号传过去
                                //然后H5刷新订单页面
                                //而这个调用H5的动作的发起，也是这个类，因为这个activity启动模式是standard，
                                //同上面的注释一样，这栈中，当前这个Activity前面还有N个这个名字的Activity对象
                                //而我做这个刷新动作，必须要在订单页执行，而我支付成功后，返回去的页面就一定是那个订单页，
                                //所以现在相当于我在N个JSInteractionActivity中发送N个Rxbus消息，在N个JSInteractionActivity
                                //中接收这个消息。那么当我返回去的时候，H5那边有个方法正好是refresh_order，所以在N个JSInteractionActivity
                                //中监听RxBus消息，然后就能达到目的了。
                                RefreshH5PayStateEvent event = (RefreshH5PayStateEvent) o;
                                refreshH5StateEvent(event.getOrder_number());
                            } else if (o instanceof RefreshWhenDeleteArticleEvent) {
                                getView().getWebView().reload();
                            }
                        }));
    }

    public String getUrl() {
        return mModel.getUrl();
    }

    private void showCallNumDialog(JSCallPhoneNumberEvent event) {
        String tel = event.getTel();
        DialogUtils.getBuilder(getView())
                .content(String.format(getView().getString(R.string.confirm_call_x), tel))
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            requestPermission(event);
                            break;
                    }
                }).show();
    }

    private void requestPermission(JSCallPhoneNumberEvent event) {
        RxPermissions.getInstance(getView())
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        deductionWhenCall(event);
                    }
                });
    }

    //H5打电话
    private void deductionWhenCall(JSCallPhoneNumberEvent event) {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.deductionWhenCall(event.getShop_id(), getUserPhoneNum()
                        , event.getShop_service_id())
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (!"true".equalsIgnoreCase(trueEntity.getData().getResult()))
                                    return;
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + event.getTel()));
                                getView().startActivity(intent);
                            }
                        }));
    }

    private String getUserPhoneNum() {
        String extraPhoneNumber = UserManager.INSTANCE.getUserInfo(UserManager.KEY_PHONE_NUMBER);
        return TextUtils.isEmpty(AppUtils.getLine1Number()) ? extraPhoneNumber : AppUtils.getLine1Number();
    }

    public void deleteArticle() {
        if (TextUtils.isEmpty(mArticleId)) return;
        DialogUtils.getBuilder(getView())
                .content(R.string.confirm_delete_article)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            deleteArticle_();
                            break;
                    }
                })
                .show();
    }

    //删除帖子
    private void deleteArticle_() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.deleteArticle(mArticleId)
                        .subscribe(new Observer<TrueEntity>() {
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
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (!"true".equalsIgnoreCase(trueEntity.getData().getResult()))
                                    return;
                                ToastUtils.show(getView().getString(R.string.delete_success));
                                RxBus.INSTANCE.send(new RefreshWhenDeleteArticleEvent());
                                getView().finish();
                            }
                        }));
    }

    //H5支付
    private void h5Pay(PayEntity entity) {
        PayEntity.ParamsBean bean = entity.getParams();
        if (bean == null) return;
        PayEntity.ParamsBean.UrlBean url = bean.getUrl();
        if (url == null) return;
        String back_type = url.getBack_type();
        int type = url.getType();
        String order_number = url.getOrder_number();

        PaymentsEntity paymentsEntity = new PaymentsEntity();
        PaymentsEntity.DataBean dataBean = new PaymentsEntity.DataBean();

        switch (type) {
            case 1:

                break;
            case 2:
                PayEntity.ParamsBean.UrlBean.AlipayBean alipay = url.getAlipay();
                if (alipay == null
                        || TextUtils.isEmpty(alipay.getParms_str())
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
                break;
            case 4:
                break;
            default:
                break;
        }

        PayManager.with().loadPay(new OnPayListener() {
            @Override
            public void onSuccess() {
                paySuccess(back_type, order_number);
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

    //支付成功后通知调用H5的js,刷新网页
    private void refreshH5StateEvent(String order_number) {
        getView().onLineMethod("refresh_order(" + order_number + ")");
    }

    /**
     * 银联支付之后的结果回调
     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null) {
//            return;
//        }
//        String msg = "";
//        String str = data.getExtras().getString("pay_result");
//        if (str.equalsIgnoreCase("success")) {
//            if (data.hasExtra("result_data")) {
//                String result = data.getExtras().getString("result_data");
//                try {
//                    JSONObject resultJson = new JSONObject(result);
//                    String sign = resultJson.getString("sign");
//                    String dataOrg = resultJson.getString("data");
//                    boolean ret = verify(dataOrg, sign, mMode);
//                    if (ret) {
//                        paySuccess();
//                    } else {
//                        payFailed();
//                    }
//                } catch (JSONException e) {
//                }
//            } else {
//                paySuccess();
//            }
//        } else if (str.equalsIgnoreCase("fail")) {
//            payFailed();
//        } else if (str.equalsIgnoreCase("cancel")) {
//            payCancel();
//        }
//    }

    private void payCancel() {
        ToastUtils.show(getView().getString(R.string.result_pay_cancel));
    }

    private void payFailed() {
        ToastUtils.show(getView().getString(R.string.result_pay_failed));
    }

    private void paySuccess(String back_type, String order_number) {
        ToastUtils.show(getView().getString(R.string.result_pay_success));
        new Handler().postDelayed(() -> {
            if (getView() != null) {
                //如果这个字段不是空的，就关闭当前Activity，返回上一页
                // (需验证其他支付方式的界面，再支付完毕之后会不会回到 选择支付方式的界面，如果不回去，则需要另寻出路)
                if (TextUtils.isEmpty(back_type)) {
                    new AcHelper.Builder(getView())
                            .closeCurrent(true)
                            .exitAnim(R.anim.fade_out)
                            .target(TabHomeActivity.class)
                            .start();
                } else {
                    RxBus.INSTANCE.send(new RefreshH5PayStateEvent(order_number));
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
