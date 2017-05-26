package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangeUserInfoActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

@Presenter(MedicalChangeUserInfoActivityPresenter.class)
public class MedicalChangeUserInfoActivity extends AppBaseActivity<MedicalChangeUserInfoActivityPresenter> {

    @Bind(R.id.iv_clear_input)
    ImageView mIvClear;
    @Bind(R.id.et_nick_name)
    EditText mEtNickName;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_change_user_info);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mEtNickName.setText(getPresenter().getData());
        if (!TextUtils.isEmpty(getPresenter().getData()))
            mEtNickName.setSelection(getPresenter().getData().length());

    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    @Override
    public String getLayoutTitle() {
        return getPresenter().getTitle();
    }

    public EditText getEtNickName() {
        return mEtNickName;
    }

}
