package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.techjumper.commonres.ComConstant;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.MainActivity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    @OnClick(R.id.title_img)
    void titleImg() {
        if (ComConstant.titleFinish) {
            getView().finish();
        }
    }

    @OnClick(R.id.title)
    void title() {
        if (ComConstant.titleUpdate) {
            PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
                @Override
                public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                    try {
                        pluginExecutor.send(PluginEngine.CODE_UPDATE_PLUGIN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onEngineDisconnected() {

                }
            });
        }
    }

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
