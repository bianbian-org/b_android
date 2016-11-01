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
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.event.JSCallPhoneNumberEvent;
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

    private Subscription mSubs1, mSubs2;

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
                                showCallNumDialog(event);
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

}
