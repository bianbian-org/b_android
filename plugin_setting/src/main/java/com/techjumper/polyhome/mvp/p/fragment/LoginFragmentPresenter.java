package com.techjumper.polyhome.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.commonres.entity.LoginEntity;
import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.UserManager;
import com.techjumper.polyhome.mvp.m.LoginFragmentModel;
import com.techjumper.polyhome.mvp.v.fragment.LoginFragment;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/6.
 */
public class LoginFragmentPresenter extends AppBaseFragmentPresenter<LoginFragment> {
    private LoginFragmentModel loginFragmentModel = new LoginFragmentModel(this);

    @OnClick(R.id.login)
    public void onClick(View view) {
        checkAndLogin();
    }

    private void checkAndLogin() {
        EditText et = null;
        if (!StringUtils.PATTERN_PASSWORD.matcher(getView().getLoginPasswordInput().getText().toString()).matches()) {
            et = getView().getLoginPasswordInput();
            getView().setText(et, et.getText());
        }
        if (!StringUtils.PATTERN_MOBILE.matcher(getView().getLoginMobileInput().getText().toString()).matches()) {
            et = getView().getLoginMobileInput();
            getView().setText(et, et.getText());
        }

        login();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        //用正则判断输入是否规范
        setOnTextChangeListener(getView().getLoginMobileInput()
                , StringUtils.PATTERN_MOBILE
                , 11
                , getView().getString(R.string.error_wrong_phone_number));

        setOnTextChangeListener(getView().getLoginPasswordInput()
                , StringUtils.PATTERN_PASSWORD
                , 20
                , getView().getString(R.string.error_wrong_password));

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

    private void login() {
        getView().showLoading(false);
        addSubscription(
                loginFragmentModel.login(getView().getLoginMobileInput().getText().toString()
                        , getView().getLoginPasswordInput().getText().toString())
                        .subscribe(new Subscriber<LoginEntity>() {
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
                            public void onNext(LoginEntity entity) {
                                if (!processNetworkResult(entity)) {
                                    getView().dismissLoading();
                                    return;
                                }
                                UserManager.INSTANCE.saveUserInfo(entity);
                                UserManager.INSTANCE.notifyLoginOrLogoutEvent(true);
                            }
                        })
        );
    }
}
