package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
public class AdActivityPresenter extends AppBaseActivityPresenter<AdActivity> {

    @OnClick(R.id.bottom_back)
    void back() {
        getView().finish();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
