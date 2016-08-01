package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.RegistActivityPresenter;
import com.techjumper.polyhomeb.service.CountdownService;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(RegistActivityPresenter.class)
public class RegistActivity extends AppBaseActivity<RegistActivityPresenter> {

    @Bind(R.id.tv_psw)
    TextView mTvPsw;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_verification_code)
    EditText mEtVerification;
    @Bind(R.id.et_password)
    EditText mEtPsw;
    @Bind(R.id.tv_get_verification_code)
    TextView mTvGetVerification;
    @Bind(R.id.tv_regist)
    TextView mTvRegist;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_regist);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvPsw.setText(String.format(getString(R.string.regist_password), "   "));
        loadPhoneNumberToEdit();
        initVerificationBtn();
    }

    private void initVerificationBtn() {
        if (CountdownService.isCountingDown()) {
            onCountDownChange(CountdownService.getCurrCount());
        }
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.login_regist);
    }

    private void loadPhoneNumberToEdit() {
        mEtPhone.setText(getPresenter().getPhoneNumber());
        setSelectionToEnd(mEtPhone);
    }

    public void setVerificationCode(String code) {
        mEtVerification.setText(code);
        setSelectionToEnd(mEtVerification);
    }

    private void setSelectionToEnd(EditText et) {
        et.setSelection(et.getText().length());
    }

    public void onCountDownChange(int count) {
        String text = count == 0 ? getString(R.string.regist_get_verification_code) : count + getString(R.string.regist_get_verification_later);
        boolean isEnable = count == 0;
        mTvGetVerification.setText(text);
        mTvGetVerification.setEnabled(isEnable);
    }

    public EditText getEtVerification() {
        return mEtVerification;
    }

    public EditText getEtPhone() {
        return mEtPhone;
    }

    public EditText getEtPsw() {
        return mEtPsw;
    }

    public void showError(EditText editText, CharSequence message) {
        editText.setError(message);
    }

    public String getPhoneNumber() {
        return mEtPhone.getText().toString();
    }

    public void showKeyboard(EditText et) {
        KeyboardUtils.showKeyboard(et);
        setSelectionToEnd(et);
        et.postDelayed(et::selectAll, 500);
    }

    public void setText(EditText et, Editable text) {
        et.setText(text);
    }

}
