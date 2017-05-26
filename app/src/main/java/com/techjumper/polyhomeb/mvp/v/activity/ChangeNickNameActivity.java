package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.ChangeNickNameActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ChangeNickNameActivityPresenter.class)
public class ChangeNickNameActivity extends AppBaseActivity<ChangeNickNameActivityPresenter> {

    @Bind(R.id.iv_clear_input)
    ImageView mIvClear;
    @Bind(R.id.et_nick_name)
    EditText mEtNickName;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_change_nick_name);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mEtNickName.setText(getPresenter().getNickName());
        if (!TextUtils.isEmpty(getPresenter().getNickName()))
            mEtNickName.setSelection(getPresenter().getNickName().length());
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.change_nick_name);
    }

    public EditText getEtNickName() {
        return mEtNickName;
    }

    @Override
    public void onBackPressed() {
        getPresenter().sendNickName();
        super.onBackPressed();
    }
}
