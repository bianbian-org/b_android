package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangeLoginAccountActivityPresenter;
import com.techjumper.polyhomeb.user.UserManager;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MedicalChangeLoginAccountActivityPresenter.class)
public class MedicalChangeLoginAccountActivity extends AppBaseActivity<MedicalChangeLoginAccountActivityPresenter> {

    @Bind(R.id.et_account)
    EditText mEtAccount;
    @Bind(R.id.et_password)
    EditText mEtPsw;
    @Bind(R.id.tv_current_account)
    TextView mTvCurrentAccount;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_change_login_account);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showCurrentUserInfo();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.medical_change_account);
    }

    public void showCurrentUserInfo() {
        String nickName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_NICK_NAME);
        String pName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_P_NAME);
        mTvCurrentAccount.setText(TextUtils.isEmpty(pName) ? nickName : pName);
    }

    public EditText getEtAccount() {
        return mEtAccount;
    }

    public EditText getEtPsw() {
        return mEtPsw;
    }
}
