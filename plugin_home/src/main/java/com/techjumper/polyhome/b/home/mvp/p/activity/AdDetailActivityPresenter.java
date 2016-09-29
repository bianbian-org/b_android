package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdDetailActivity;
import com.techjumper.polyhome_b.adlib.receiver.CommonReceiver;
import com.techjumper.polyhome_b.adlib.window.AdWindowManager;

import butterknife.OnClick;

/**
 * Created by kevin on 16/9/28.
 */

public class AdDetailActivityPresenter extends AppBaseActivityPresenter<AdDetailActivity> {

    @OnClick(R.id.bottom_back)
    void back() {
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
        Intent intent = new Intent();
        intent.setAction(CommonReceiver.ACTION_CLOSE_AD_WINDOW);
        getView().sendBroadcast(intent);
    }
}
