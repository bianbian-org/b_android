package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.MemberManageActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MemberManageActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MemberManageActivityPresenter extends AppBaseActivityPresenter<MemberManageActivity> {

    private MemberManageActivityModel mModel = new MemberManageActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
