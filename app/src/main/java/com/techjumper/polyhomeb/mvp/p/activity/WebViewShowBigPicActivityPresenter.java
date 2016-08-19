package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.WebViewShowBigPicActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.WebViewShowBigPicActivity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WebViewShowBigPicActivityPresenter extends AppBaseActivityPresenter<WebViewShowBigPicActivity> {

    private WebViewShowBigPicActivityModel mModel = new WebViewShowBigPicActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public int getIndex() {
        return mModel.getIndex();
    }

    public List<String> getPicList() {
        return mModel.getPicList();
    }
}
