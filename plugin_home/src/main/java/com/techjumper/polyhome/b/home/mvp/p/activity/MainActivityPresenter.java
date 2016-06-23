package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.plugincommunicateengine.IPluginMessageReceiver;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.v.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

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

    @OnClick(R.id.vedio)
    void callVedio() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.VideoSurveillanceActivity");
        it.setComponent(componentName);
        getView().startActivity(it);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        PluginEngineUtil.initUserInfo();

        PluginEngine.getInstance().registerReceiver((code, message, extras) -> {
            if (code == PluginEngine.CODE_GET_SAVE_INFO) {
                SaveInfoEntity saveInfoEntity = GsonUtils.fromJson(message, SaveInfoEntity.class);
                if (saveInfoEntity == null || saveInfoEntity.getData() == null)
                    return;

                Log.d("plugin", "name: " + saveInfoEntity.getData().getName());
                HashMap<String, String> hashMap = saveInfoEntity.getData().getValues();
                if (hashMap == null || hashMap.size() == 0)
                    return;

                UserInfoEntity userInfoEntity = new UserInfoEntity();
                
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    Log.d("value", entry.getValue());
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("id")) {
                        userInfoEntity.setId(Long.parseLong(value));
                    } else if (key.equals("family_name")) {
                        userInfoEntity.setFamily_name(value);
                    } else if (key.equals("user_id")) {
                        userInfoEntity.setUser_id(Long.parseLong(value));
                    } else if (key.equals("ticket")) {
                        userInfoEntity.setTicket(value);
                    } else if (key.equals("has_binding")) {
                        userInfoEntity.setHas_binding(Integer.parseInt(value));
                    }
                }

                UserInfoManager.saveUserInfo(userInfoEntity);

                RxBus.INSTANCE.send(new UserInfoEvent(userInfoEntity));
            }
        });
    }
}
