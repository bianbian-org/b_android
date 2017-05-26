package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.ChangeEmailActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ChangeEmailActivityPresenter.class)
public class ChangeEmailActivity extends AppBaseActivity<ChangeEmailActivityPresenter> {

    @Bind(R.id.iv_clear_input)
    ImageView mIvClear;
    @Bind(R.id.et_email)
    EditText mEtEmail;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_change_email);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mEtEmail.setText(getPresenter().getEtEmail());
        if (!TextUtils.isEmpty(getPresenter().getEtEmail()))
            mEtEmail.setSelection(getPresenter().getEtEmail().length());
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.change_email);
    }

    public EditText getEtEmail() {
        return mEtEmail;
    }

    @Override
    public void onBackPressed() {
        getPresenter().sendEmail();
        super.onBackPressed();
    }

}
