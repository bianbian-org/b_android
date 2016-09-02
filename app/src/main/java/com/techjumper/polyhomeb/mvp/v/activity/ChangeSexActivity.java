package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.ChangeSexActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ChangeSexActivityPresenter.class)
public class ChangeSexActivity extends AppBaseActivity<ChangeSexActivityPresenter> {

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
        return inflate(R.layout.activity_change_sex);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String sex = getPresenter().getSex();
        getPresenter().mSex = sex;
        if (!TextUtils.isEmpty(sex)) {
            if (getString(R.string.male).equals(sex)) {
                mIvMale.setVisibility(View.VISIBLE);
                mIvFemale.setVisibility(View.GONE);
            } else if (getString(R.string.female).equals(sex)) {
                mIvMale.setVisibility(View.GONE);
                mIvFemale.setVisibility(View.VISIBLE);
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
    public String getLayoutTitle() {
        return getString(R.string.change_sex);
    }

    @Override
    public void onBackPressed() {
        getPresenter().sendSex();
        super.onBackPressed();
    }

    public ImageView getIvMale() {
        return mIvMale;
    }

    public ImageView getIvFemale() {
        return mIvFemale;
    }
}
