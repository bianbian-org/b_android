package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.LoginActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(LoginActivityPresenter.class)
public class LoginActivity extends AppBaseActivity<LoginActivityPresenter> {

    @Bind(R.id.et_account)
    EditText mEtAccount;
    @Bind(R.id.et_password)
    EditText mEtPsw;
    @Bind(R.id.layout_input_wrong)
    LinearLayout mLayoutWrong;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_regist)
    TextView mTvRegist;
    @Bind(R.id.tv_forget_psw)
    TextView mTvForgetPsw;
    @Bind(R.id.iv_left_icon)
    ImageView mIvBack;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_login);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mIvBack.setVisibility(View.INVISIBLE);
    }

    @Override
    protected boolean onTitleLeftClick() {
        return true;
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.login_login);
    }

    public String getPhoneNumber() {
        return mEtAccount.getText().toString();
    }

    public EditText getEtAccount() {
        return mEtAccount;
    }

    public EditText getEtPsw() {
        return mEtPsw;
    }

    public void setText(EditText et, Editable text) {
        et.setText(text);
    }

    public void showKeyboard(EditText et) {
        KeyboardUtils.showKeyboard(et);
        setSelectionToEnd(et);
        et.postDelayed(et::selectAll, 500);

    }

    private void setSelectionToEnd(EditText et) {
        et.setSelection(et.getText().length());
    }

    public void clearPassword() {
        mEtPsw.setText("");
    }

    public LinearLayout getLayoutWrong() {
        return mLayoutWrong;
    }


    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
    }
}
