package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangUserSexActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

@Presenter(MedicalChangUserSexActivityPresenter.class)
public class MedicalChangUserSexActivity extends AppBaseActivity<MedicalChangUserSexActivityPresenter> {

    @Bind(R.id.layout_male)
    ViewGroup mLayoutMale;
    @Bind(R.id.layout_female)
    ViewGroup mLayoutFemale;

    @Bind(R.id.iv_male)
    ImageView mIvMale;
    @Bind(R.id.iv_femle)
    ImageView mIvFemale;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_change_user_sex);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String sex = getPresenter().getSex();  //1  2的字符串,或者空
        if (!TextUtils.isEmpty(sex)) {
            int i = Integer.parseInt(sex);
            if (1 == i) {
                mIvMale.setVisibility(View.VISIBLE);
                mIvFemale.setVisibility(View.GONE);
                getPresenter().mSex_ = 1;
            } else if (2 == i) {
                mIvMale.setVisibility(View.GONE);
                mIvFemale.setVisibility(View.VISIBLE);
                getPresenter().mSex_ = 2;
            } else {
                mIvMale.setVisibility(View.GONE);
                mIvFemale.setVisibility(View.GONE);
            }
        } else {
            mIvMale.setVisibility(View.GONE);
            mIvFemale.setVisibility(View.GONE);
        }
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
        return getString(R.string.change_sex);
    }

    public ImageView getIvMale() {
        return mIvMale;
    }

    public ImageView getIvFemale() {
        return mIvFemale;
    }

}
