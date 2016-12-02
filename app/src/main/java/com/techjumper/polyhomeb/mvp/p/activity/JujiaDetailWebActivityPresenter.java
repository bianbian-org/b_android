package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.JujiaDetailWebModel;
import com.techjumper.polyhomeb.mvp.v.activity.JujiaDetailWebActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JujiaDetailWebActivityPresenter extends AppBaseActivityPresenter<JujiaDetailWebActivity> {

    private JujiaDetailWebModel mModel = new JujiaDetailWebModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick({R.id.close_group, R.id.back_group, R.id.refresh_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_group:
                goBack();
                return;
            case R.id.back_group:
                closePage();
                break;
            case R.id.refresh_group:
                reload();
                break;
        }
    }

    private void closePage() {
        getView().finish();
    }

    private void goBack() {
        if (getView().getWebView() == null) return;
        if (!getView().getWebView().canGoBack()) return;
        getView().getWebView().goBack();
    }

    private void reload() {
        if (getView().getWebView() == null) return;
        getView().getWebView().reload();
    }

    public String getFirstPageUrl() {
        return mModel.getFirstPageUrl();
    }

    public String getSecondPageUrl() {
        return mModel.getSecondPageUrl();
    }

    public String getThirdPageUrl() {
        return mModel.getThirdPageUrl();
    }

    public String getMorePageUrl() {
        return mModel.getMorePageUrl();
    }
}
