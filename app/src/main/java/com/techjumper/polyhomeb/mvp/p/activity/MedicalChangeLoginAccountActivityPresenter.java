package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.MedicalChangeLoginAccountActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeAccountActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeLoginAccountActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalChangeLoginAccountActivityPresenter extends AppBaseActivityPresenter<MedicalChangeLoginAccountActivity> {

    private MedicalChangeLoginAccountActivityModel mModel = new MedicalChangeLoginAccountActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.layout_current_account)
    public void onClick(View view) {
        new AcHelper.Builder(getView()).target(MedicalChangeAccountActivity.class).start();
    }
}
