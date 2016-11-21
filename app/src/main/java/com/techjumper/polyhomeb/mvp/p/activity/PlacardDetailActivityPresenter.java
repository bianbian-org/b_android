package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.PlacardDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.PlacardDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardDetailActivityPresenter extends AppBaseActivityPresenter<PlacardDetailActivity> {

    private PlacardDetailActivityModel mModel = new PlacardDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public String getType() {
        return mModel.getType();
    }

    public String getTime() {
        return mModel.getTime();
    }

    public String getTitle() {
        return mModel.getTitle();
    }

    public int getId() {
        return mModel.getId();
    }

    public String getContent() {
        return mModel.getContent();
    }
}
