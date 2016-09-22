package com.techjumper.polyhomeb.mvp.p.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.CountdownEvent;
import com.techjumper.polyhomeb.entity.event.MedicalChangeUserInfoEvent;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalVerificationCodeEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalChangUserEmailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangUserEmailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.service.CountdownService;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Headers;
import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalChangUserEmailActivityPresenter extends AppBaseActivityPresenter<MedicalChangUserEmailActivity> {

    private MedicalChangUserEmailActivityModel mModel = new MedicalChangUserEmailActivityModel(this);

    private Subscription mSubs1, mSubs2;

    private String mCookie = "";

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
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

    }

    @OnClick({R.id.tv_get_verification_code, R.id.layout_clear_input})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_verification_code:
                sendVerificationCode(buildCodeMap());
                break;
            case R.id.layout_clear_input:
                getView().getEtEmailName().setText("");
                break;
        }
    }

    public void onTitleRightClick() {
        //if (code是空的或者新输入的邮件地址是空的) return;
        if (TextUtils.isEmpty(getView().getEtEmailName().getEditableText().toString())) {
            ToastUtils.show(getView().getString(R.string.medical_input_email_is_null));
            return;
        }
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.changeUserEmailInfo(mCookie
                        , buildChangeInfoMap())
                        .subscribe(new Observer<Result<MedicalStatusEntity>>() {
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
                            public void onNext(Result<MedicalStatusEntity> result) {
                                MedicalStatusEntity medicalStatusEntity = result.response().body();
                                if (449 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_token_overdue));
                                    new AcHelper.Builder(getView()).closeCurrent(true).target(MedicalLoginActivity.class).start();
                                }
                                if (medicalStatusEntity == null) return;
                                int status = medicalStatusEntity.getStatus();

                                switch (status) {
                                    case 1:
                                        ToastUtils.show(getView().getString(R.string.medical_change_success));
                                        UserManager.INSTANCE.saveUserInfo(UserManager.INSTANCE.KEY_MEDICAL_CURRENT_USER_EMAIL, getView().getEtEmailName().getEditableText().toString());
                                        RxBus.INSTANCE.send(new MedicalChangeUserInfoEvent(mModel.getType(), getView().getEtEmailName().getEditableText().toString(), mModel.getPosition()));
                                        getView().finish();
                                        break;
                                    case 3:
                                        ToastUtils.show(getView().getString(R.string.medical_email_exist));
                                        break;
                                    case 7:
                                        ToastUtils.show(getView().getString(R.string.medical_email_wrong));
                                        break;
                                    case 10:
                                        ToastUtils.show(getView().getString(R.string.medical_email_code_wrong));
                                        break;
                                    case 12:
                                        ToastUtils.show(getView().getString(R.string.medical_email_wrong__));
                                        break;
                                    case 17:
                                        ToastUtils.show(getView().getString(R.string.medical_email_wrong___));
                                        break;
                                    case -1:
                                        ToastUtils.show(getView().getString(R.string.medical_unknow_error));
                                        break;
                                }
                            }
                        })
        );
    }

    private void sendVerificationCode(Map<String, String> map) {
        if (TextUtils.isEmpty(getView().getEtEmailName().getEditableText().toString())) {
            ToastUtils.show(getView().getString(R.string.medical_input_email_is_null));
            return;
        }
        getView().showLoading(false);
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getVerificationCode(map)
                        .subscribe(new Observer<Result<MedicalVerificationCodeEntity>>() {
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
                            public void onNext(Result<MedicalVerificationCodeEntity> result) {
                                MedicalVerificationCodeEntity medicalVerificationCodeEntity = result.response().body();
                                if (medicalVerificationCodeEntity == null) return;
                                if (449 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_token_overdue));
                                    new AcHelper.Builder(getView()).closeCurrent(true).target(MedicalLoginActivity.class).start();
                                }
                                int status = medicalVerificationCodeEntity.getStatus();
                                switch (status) {
                                    case 1:
                                        getView().showHint(String.format(getView().getString(R.string.medical_change_email_send_code_to)
                                                , getView().getEtEmailName().getEditableText().toString()));
                                        startCountdown();
                                        //String code = medicalVerificationCodeEntity.getCode(); //邮件验证码
                                        Headers headers = result.response().headers();
                                        mCookie = headers.get("Set-Cookie");  //headers.get("Cookie")
                                        break;
                                    case 2:
                                        ToastUtils.show(getView().getString(R.string.medical_email_wrong___));
                                        break;
                                    case 3:
                                        ToastUtils.show(getView().getString(R.string.medical_code_not_send));
                                        break;
                                    case 4:
                                        ToastUtils.show(getView().getString(R.string.medical_email_address_exist));
                                        break;
                                    case 5:
                                        ToastUtils.show(getView().getString(R.string.medical_unknow_error));
                                        break;
                                }
                            }
                        })
        );
    }

    private void startCountdown() {
        Activity activity = getView();
        if (activity == null) return;
        activity.startService(new Intent(activity, CountdownService.class));
    }

    //构建发送验证码时候的map
    private Map<String, String> buildCodeMap() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        map.put("email", getView().getEtEmailName().getEditableText().toString());
        return map;
    }

    //构建修改邮箱的map
    private Map<String, String> buildChangeInfoMap() {
        Map<String, String> map = new HashMap<>();
        map.put("email", getView().getEtEmailName().getEditableText().toString());
        map.put("emailcode", getView().getEtCode().getEditableText().toString());
        return map;
    }

    public String getData() {
        return mModel.getData();
    }

}
