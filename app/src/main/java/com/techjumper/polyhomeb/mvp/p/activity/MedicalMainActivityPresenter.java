package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.MedicalMainActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeLoginAccountActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalUserInfoActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainActivityPresenter extends AppBaseActivityPresenter<MedicalMainActivity> {

    private MedicalMainActivityModel mModel = new MedicalMainActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public void onTitleRightClick() {
        new AcHelper.Builder(getView()).target(MedicalChangeLoginAccountActivity.class).start();
    }

    @OnClick(R.id.layout_user_info)
    public void onClick(View view) {
        new AcHelper.Builder(getView()).target(MedicalUserInfoActivity.class).start();
    }
}
