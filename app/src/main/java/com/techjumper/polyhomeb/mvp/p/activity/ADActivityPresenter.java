package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.ADActivityModel;
import com.techjumper.polyhomeb.mvp.p.activity.AppBaseActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.activity.ADActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ADActivityPresenter extends AppBaseActivityPresenter<ADActivity> {

    private ADActivityModel mModel = new ADActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public String getUrl() {
        return mModel.getUrl();
    }
}
