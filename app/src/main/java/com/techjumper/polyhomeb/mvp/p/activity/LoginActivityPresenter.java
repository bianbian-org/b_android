package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.mvp.m.LoginActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.RegistActivity;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class LoginActivityPresenter extends AppBaseActivityPresenter<LoginActivity> {

    public static final String KEY_PHONE_NUMBER = "key_phone_number";

    private LoginActivityModel mModel = new LoginActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
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
    }

    @OnClick({R.id.tv_login, R.id.tv_forget_psw, R.id.tv_regist})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PHONE_NUMBER, getView().getPhoneNumber());
        switch (view.getId()) {
            case R.id.tv_login:
                checkingInputAndLogin();
                break;
            case R.id.tv_forget_psw:
                break;
            case R.id.tv_regist:
                new AcHelper.Builder(getView()).extra(bundle).target(RegistActivity.class).start();
                break;
        }
    }

    private void checkingInputAndLogin() {
        EditText et = null;
        if (!StringUtils.PATTERN_MOBILE.matcher(getView().getPhoneNumber()).matches()) {
            et = getView().getEtAccount();
            getView().setText(et, et.getText());
//            getView().showHint(Utils.appContext.getString(R.string.error_wrong_phone_number));
            getView().getLayoutWrong().setVisibility(View.VISIBLE);
        } else {
            getView().getLayoutWrong().setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.PATTERN_PASSWORD.matcher(getView().getEtPsw().getText().toString()).matches()) {
            et = getView().getEtPsw();
            getView().setText(et, et.getText());
//            getView().showHint(Utils.appContext.getString(R.string.error_wrong_password));
            getView().getLayoutWrong().setVisibility(View.VISIBLE);
        } else {
            getView().getLayoutWrong().setVisibility(View.INVISIBLE);
        }
        if (et != null) {
            getView().showKeyboard(et);
        } else {
            login();
        }
    }

    private void login() {
        getView().showLoading();
        addSubscription(
                mModel.login(getView().getPhoneNumber()
                        , getView().getEtPsw().getText().toString())
                        .subscribe(new Subscriber<LoginEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                            }

                            @Override
                            public void onNext(LoginEntity entity) {
                                if (!processNetworkResult(entity)) {
                                    getView().dismissLoading();
                                    getView().clearPassword();
                                    return;
                                }
                                UserManager.INSTANCE.saveUserInfo(entity);
                            }
                        })
        );
    }
}
