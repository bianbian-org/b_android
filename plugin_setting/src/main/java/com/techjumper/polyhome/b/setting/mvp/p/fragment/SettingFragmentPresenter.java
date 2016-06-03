package com.techjumper.polyhome.b.setting.mvp.p.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.commonres.entity.LoginEntity;
import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.setting.R;
import com.techjumper.polyhome.b.setting.SettingManager;
import com.techjumper.polyhome.b.setting.UserManager;
import com.techjumper.polyhome.b.setting.mvp.m.SettingFragmentModel;
import com.techjumper.polyhome.b.setting.mvp.v.fragment.SettingFragment;
import com.techjumper.polyhome.b.setting.utils.DateUtil;
import com.techjumper.polyhome.b.setting.utils.StringUtil;
import com.techjumper.polyhome.b.setting.widget.SettingDatePickerDialog;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/10.
 */
public class SettingFragmentPresenter extends AppBaseFragmentPresenter<SettingFragment> {

    private int sex = LoginEntity.MALE;

    SettingFragmentModel settingFragmentModel = new SettingFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

        setOnTextChangeListener(getView().getSplPassword()
                , StringUtils.PATTERN_PASSWORD
                , 20
                , getView().getString(R.string.error_wrong_password));

        setOnTextChangeListener(getView().getSupOldpasswordInput()
                , StringUtils.PATTERN_PASSWORD
                , 20
                , getView().getString(R.string.error_wrong_password));

        setOnTextChangeListener(getView().getSupNewpasswordInput()
                , StringUtils.PATTERN_PASSWORD
                , 20
                , getView().getString(R.string.error_wrong_password));

        setOnTextChangeListener(getView().getSupConfirmpasswordInput()
                , StringUtils.PATTERN_PASSWORD
                , 20
                , getView().getString(R.string.error_wrong_password));
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.logout)
    void logout_tv() {
        logout();
    }

    /**
     * 获取日期（生日）
     */
    @OnClick(R.id.sui_birthday_input)
    void getDate() {
        pickDate();
    }

    /**
     * 保存修改用户登录密码
     */
    @OnClick(R.id.sup_save)
    void changePasswordSave() {
        checkAndChangePassword();
    }

    /**
     * 保存用户信息
     */
    @OnClick(R.id.sui_save)
    void updateUserInfo() {
        updateInfo();
    }

    /**
     * 选择性别
     *
     * @param isFemale
     */
    @OnCheckedChanged(R.id.sui_female)
    void checkFemalle(boolean isFemale) {
        if (isFemale) {
            sex = LoginEntity.FRMALE;
        } else {
            sex = LoginEntity.MALE;
        }
    }

    /**
     * 功能密码登录
     */
    @OnClick(R.id.spl_login)
    void loginSpl() {
        String password = getView().getSplPassword().getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(getView().getString(R.string.setting_password_null));
            return;
        }

        if (password.length() < 8 || password.length() > 20)
            return;

        if (SettingManager.INSTANCE.isPassword(password)) {
            getView().showSettingMain();
        } else {
            ToastUtils.show(getView().getString(R.string.setting_password_error));
        }
    }

    private void checkAndChangePassword() {
        EditText et = null;
        if (!StringUtils.PATTERN_PASSWORD.matcher(getView().getSupOldpasswordInput().getText().toString()).matches()) {
            et = getView().getSupOldpasswordInput();
            getView().setText(et, et.getText());
        }
        if (!StringUtils.PATTERN_PASSWORD.matcher(getView().getSupNewpasswordInput().getText().toString()).matches()) {
            et = getView().getSupNewpasswordInput();
            getView().setText(et, et.getText());
        }
        if (!StringUtils.PATTERN_PASSWORD.matcher(getView().getSupConfirmpasswordInput().getText().toString()).matches()) {
            et = getView().getSupConfirmpasswordInput();
            getView().setText(et, et.getText());
        }

        changePassword();
    }

    public void logout() {
        DialogUtils.getBuilder(getView().getActivity())
                .content(R.string.confirm_logout)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            UserManager.INSTANCE.logout();
                            UserManager.INSTANCE.notifyLoginOrLogoutEvent(false);
                            break;
                    }
                })
                .show();
    }

    public void pickDate() {

        Calendar calendar = Calendar.getInstance();

        new SettingDatePickerDialog(getView().getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            getView().getSuiBirthdayInput().setText(DateUtil.getDateLink(year, monthOfYear + 1, dayOfMonth, "-"));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
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

    public void changePassword() {
        String oldPwd = getView().getSupOldpasswordInput().getText().toString();
        String newPwd = getView().getSupNewpasswordInput().getText().toString();
        String confirmNewPwd = getView().getSupConfirmpasswordInput().getText().toString();

        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtils.show(getView().getString(R.string.setting_password_old_null));
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.show(getView().getString(R.string.setting_password_new_null));
            return;
        }

        if (TextUtils.isEmpty(confirmNewPwd)) {
            ToastUtils.show(getView().getString(R.string.setting_password_confirm_new_null));
            return;
        }

        getView().showLoading(false);
        addSubscription(settingFragmentModel.changePassword(oldPwd, newPwd, confirmNewPwd)
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
                    public void onNext(LoginEntity loginEntity) {
                        if (!processNetworkResult(loginEntity)) {
                            getView().dismissLoading();
                            return;
                        }
                        UserManager.INSTANCE.saveUserInfo(loginEntity);
                        UserManager.INSTANCE.notifyLoginOrLogoutEvent(false);

                        ToastUtils.show(getView().getString(R.string.setting_password_success));
                    }
                }));
    }

    private void updateInfo() {
        String nickname = getView().getSuiNicknameInput().getText().toString();
        String birthday = getView().getSuiBirthdayInput().getText().toString();
        String email = getView().getSuiEmailInput().getText().toString();
        String sexString = String.valueOf(sex);

        if (!StringUtil.isEmail(email)) {
            ToastUtils.show(getView().getString(R.string.setting_user_info_email_no));
            return;
        }

        getView().showLoading(false);

        addSubscription(settingFragmentModel.updateUserInfo(nickname, sexString, birthday, email).subscribe(new Subscriber<LoginEntity>() {
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
            public void onNext(LoginEntity loginEntity) {
                if (!processNetworkResult(loginEntity)) {
                    getView().dismissLoading();
                    return;
                }
                UserManager.INSTANCE.saveUserInfo(loginEntity);

                ToastUtils.show(getView().getString(R.string.setting_user_info_success));
            }
        }));
    }
}
