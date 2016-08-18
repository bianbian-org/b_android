package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.ReplyDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ReplyDetailActivityPresenter extends AppBaseActivityPresenter<ReplyDetailActivity> {

    private ReplyDetailActivityModel mModel = new ReplyDetailActivityModel(this);

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
