package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangeAccountActivityPresenter;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MedicalChangeAccountActivityPresenter.class)
public class MedicalChangeAccountActivity extends AppBaseActivity<MedicalChangeAccountActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_change_account);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.medical_change_account_);
    }
}
