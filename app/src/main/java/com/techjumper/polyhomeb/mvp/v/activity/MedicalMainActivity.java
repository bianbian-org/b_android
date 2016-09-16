package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalMainActivityPresenter;
import com.techjumper.polyhomeb.utils.TitleHelper;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MedicalMainActivityPresenter.class)
public class MedicalMainActivity extends AppBaseActivity<MedicalMainActivityPresenter> {

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        TitleHelper.setTitleRightText(mViewRoot, getString(R.string.medical_change_account));
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.medical);
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
}
