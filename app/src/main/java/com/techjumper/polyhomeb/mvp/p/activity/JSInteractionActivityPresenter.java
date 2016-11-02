package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.event.JSArticleIdEvent;
import com.techjumper.polyhomeb.entity.event.JSCallPhoneNumberEvent;
import com.techjumper.polyhomeb.entity.event.RefreshWhenDeleteArticleEvent;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.mvp.m.JSInteractionActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.JSInteractionActivity;
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
                                JSArticleIdEvent event = (JSArticleIdEvent) o;
                                mArticleId = event.getId();
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

}
