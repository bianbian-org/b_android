package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.MedicalLoginActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalLoginActivityPresenter extends AppBaseActivityPresenter<MedicalLoginActivity> {

    private MedicalLoginActivityModel mModel = new MedicalLoginActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_login)
    public void onClick(View view) {
        new AcHelper.Builder(getView()).target(MedicalMainActivity.class).start();
    }
}
