package com.techjumper.polyhome.b.setting.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhome.b.setting.R;
import com.techjumper.polyhome.b.setting.mvp.v.activity.SettingActivity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/5/6.
 */
public class SettingActivityPresenter extends AppBaseActivityPresenter<SettingActivity> {

    @OnClick(R.id.bottom_back)
    void back(){
        getView().finish();
    }

    @OnClick(R.id.bottom_home)
    void home() {
        getView().finish();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
