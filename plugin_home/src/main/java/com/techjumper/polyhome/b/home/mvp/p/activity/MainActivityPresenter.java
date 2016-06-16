package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.MainActivity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    @OnClick(R.id.call_service)
    void callService() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.CallingActivity");
        it.setComponent(componentName);
        it.putExtra("com.dnake.talk", "CallingActivity");
        getView().startActivity(it);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
