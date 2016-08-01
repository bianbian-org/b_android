package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxSmsReceiver;
import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.event.CountdownEvent;
import com.techjumper.polyhomeb.mvp.m.FindPasswordActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.FindPasswordActivity;
import com.techjumper.polyhomeb.service.CountdownService;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class FindPasswordActivityPresenter extends AppBaseActivityPresenter<FindPasswordActivity> {

    private FindPasswordActivityModel mModel = new FindPasswordActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        //更新验证码的输入框
//        addSubscription(
//                RxView.focusChanges(getView().getEtVerification())
//                        .skip(1)
//                        .subscribe(focus -> {
//                            getView().updateVerificationBg(focus);
//                        })
//        );

        //监听验证码倒计时
        addSubscription(
                RxBus.INSTANCE.asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof CountdownEvent) {
                                CountdownEvent event = (CountdownEvent) o;
                                getView().onCountDownChange(event.getCount());
                            }
                        })
        );

        //监听注册成功的消息
        addSubscription(
                RxBus.INSTANCE.asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof LoginEvent) {
                                LoginEvent event = (LoginEvent) o;
                                if (event.isLogin()) {
                                    getView().finish();
                                }
                            }
                        })
        );

        //用正则判断输入是否规范
        setOnTextChangeListener(getView().getEtPhone()
                , StringUtils.PATTERN_MOBILE
                , 11
                , getView().getString(R.string.error_wrong_phone_number));
        setOnTextChangeListener(getView().getEtVerification()
                , StringUtils.PATTERN_VERIFICATION_CODE
                , 6
                , getView().getString(R.string.error_wrong_verifi_code_length));
        setOnTextChangeListener(getView().getEtPsw()
                , StringUtils.PATTERN_PASSWORD
                , 20
                , getView().getString(R.string.error_wrong_password));

        //判断权限
        addSubscription(
                RxPermissions.getInstance(getView())
                        .request(Manifest.permission.RECEIVE_SMS)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                //监听短信
                                registerSmsReceiver();
                            }
                        })
        );
    }

    /**
     * 监听短信
     */
    private void registerSmsReceiver() {
        addSubscription(
                RxSmsReceiver.asObservable(getView())
                        .map(bundle -> bundle.getString(RxSmsReceiver.KEY_MESSAGE))
                        .filter(s -> !TextUtils.isEmpty(s))
                        .map(s1 -> {
                            String result = "";
                            Matcher matcher = StringUtils.PATTERN_VERIFICATION_CODE.matcher(s1);
                            if (matcher.find()) {
                                result = matcher.group();
                            }
                            return result;
                        })
                        .filter(s2 -> !TextUtils.isEmpty(s2))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                JLog.d(e.toString());
                            }

                            @Override
                            public void onNext(String s) {
                                getView().setVerificationCode(s);
                            }
                        })
        );
    }

    private void setOnTextChangeListener(EditText editText, Pattern pattern, int maxLength, String errorText) {
        addSubscription(
                RxTextView.textChanges(editText)
                        .skip(1)
                        .map(charSequence -> {
                            getView().showError(editText, null);
                            if (!TextUtils.isEmpty(charSequence)
                                    && charSequence.length() >= maxLength + 1) {
                                charSequence = charSequence.toString().substring(0, maxLength);
                                editText.setText(charSequence);
                                editText.setSelection(maxLength);
                            }
                            return charSequence;
                        })
                        .debounce(1000, TimeUnit.MILLISECONDS)
                        .map(charSequence1 -> {
                            if (!pattern.matcher(charSequence1.toString()).matches()) {
                                return errorText;
                            } else {
                                return null;
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(charSequence2 -> {
                            getView().showError(editText, charSequence2);
                        })
        );
    }

    public String getPhoneNumber() {
        return mModel.getPhoneNumber();
    }

    @OnClick({R.id.tv_regist, R.id.tv_get_verification_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_regist:
                checkingInputAndRegist();
                break;
            case R.id.tv_get_verification_code:
                sendVerificationCode();
                break;
        }
    }

    private void sendVerificationCode() {
        if (!checkPhoneNumber()) return;

        getView().showLoading(false);
        addSubscription(
                mModel.sendVerificationCode(getView().getPhoneNumber())
                        .subscribe(new Subscriber<TrueEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                            }

                            @Override
                            public void onNext(TrueEntity entity) {

                                if (!processNetworkResult(entity)) return;

                                getView().showHint(getView().getString(R.string.success_send));
                                startCountdown();

                            }
                        })
        );
    }

    private void checkingInputAndRegist() {
        EditText et = null;
        if (!StringUtils.PATTERN_PASSWORD.matcher(getView().getEtPsw().getText().toString()).matches()) {
            et = getView().getEtPsw();
            getView().setText(et, et.getText());
        }
        if (!StringUtils.PATTERN_VERIFICATION_CODE.matcher(getView().getEtVerification().getText().toString()).matches()) {
            et = getView().getEtVerification();
            getView().setText(et, et.getText());
        }
        if (!StringUtils.PATTERN_MOBILE.matcher(getView().getPhoneNumber()).matches()) {
            et = getView().getEtPhone();
            getView().setText(et, et.getText());
        }

        if (et != null) {
            getView().showKeyboard(et);
        } else {
            findPsw();
        }
    }

    private void findPsw() {
        getView().showLoading(false);
        addSubscription(
                mModel.findPassword(getView().getPhoneNumber()
                        , getView().getEtVerification().getText().toString()
                        , getView().getEtPsw().getText().toString())
                        .subscribe(new Subscriber<TrueEntity>() {
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

                                UserManager.INSTANCE.notifyLoginOrLogoutEvent(false);
                                getView().showHint(getView().getString(R.string.success_change_password));
                                getView().finish();
                            }
                        })
        );
    }

    private void startCountdown() {
        Activity activity = getView();
        if (activity == null) return;

        activity.startService(new Intent(activity, CountdownService.class));

    }

    private boolean checkPhoneNumber() {
        EditText et = null;
        if (!StringUtils.PATTERN_MOBILE.matcher(getView().getPhoneNumber()).matches()) {
            et = getView().getEtPhone();
            getView().setText(et, et.getText());
        }

        if (et != null) {
            getView().showKeyboard(et);
            return false;
        }

        return true;

    }
}
