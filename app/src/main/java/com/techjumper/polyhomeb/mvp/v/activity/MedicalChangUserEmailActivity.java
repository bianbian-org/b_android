package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangUserEmailActivityPresenter;
import com.techjumper.polyhomeb.service.CountdownService;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MedicalChangUserEmailActivityPresenter.class)
public class MedicalChangUserEmailActivity extends AppBaseActivity<MedicalChangUserEmailActivityPresenter> {

    @Bind(R.id.tv_right)
    TextView mTvRight;     //右上角文字
    @Bind(R.id.right_group)
    ViewGroup mLayoutRight;//右上角父布局
    @Bind(R.id.et_email_name)
    EditText mEtEmailName;        //邮箱地址
    @Bind(R.id.et_verification_code)
    EditText mEtCode;             //输入验证码
    @Bind(R.id.tv_get_verification_code)
    TextView mTvGetVerification;  //获取验证码

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_change_email);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mEtEmailName.setText(getPresenter().getData());
        if (!TextUtils.isEmpty(getPresenter().getData()))
            mEtEmailName.setSelection(getPresenter().getData().length());
        initVerificationBtn();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.medical_change_email);
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

    public void onCountDownChange(int count) {
        String text = count == 0 ? getString(R.string.regist_get_verification_code) : count + getString(R.string.regist_get_verification_later);
        boolean isChangeSize = count == 0 ? false : true;
        boolean isEnable = count == 0;
        mTvGetVerification.setText(text);
        mTvGetVerification.setEnabled(isEnable);
        mTvGetVerification.setTextSize(isChangeSize ? 12 : 16);
    }

    private void initVerificationBtn() {
        if (CountdownService.isCountingDown()) {
            onCountDownChange(CountdownService.getCurrCount());
        }
    }

    public EditText getEtEmailName() {
        return mEtEmailName;
    }

    public EditText getEtCode() {
        return mEtCode;
    }

    public TextView getTvRight() {
        return mTvRight;
    }

    public ViewGroup getLayoutRight() {
        return mLayoutRight;
    }

}
