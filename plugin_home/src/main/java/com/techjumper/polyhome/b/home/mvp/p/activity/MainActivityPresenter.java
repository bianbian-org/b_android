package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.plugincommunicateengine.IPluginMessageReceiver;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.home.BuildConfig;
import com.techjumper.polyhome.b.home.InfoManager;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.v.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/29.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    @OnClick(R.id.title_img)
    void titleImg() {
        if (ComConstant.titleFinish) {
            getView().finish();
        }
//        Intent intent = new Intent();
//        intent.putExtra("key_extra", "hehe");
//        intent.setAction("action_push_receive");
//        getView().sendBroadcast(intent);
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

    @OnClick(R.id.date)
    void getVersion() {
        ToastUtils.show("code: " + String.valueOf(BuildConfig.VERSION_CODE) + " name: " + BuildConfig.VERSION_NAME);
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

        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof TimeEvent) {
                        Log.d("time", "更新时间");
                        if (getView().getDate() != null) {
                            getView().getDate().setText(CommonDateUtil.getTitleDate());
                        }
                    }
                });

//        UserInfoEntity userInfoEntity2 = new UserInfoEntity();
//        userInfoEntity2.setId(438);
//        userInfoEntity2.setUser_id(367);
//        userInfoEntity2.setTicket("0f9859826eeeed6d421c0a0982ee5b08b6c41809");
//        UserInfoManager.saveUserInfo(userInfoEntity2);
//        RxBus.INSTANCE.send(new UserInfoEvent(userInfoEntity2));

//        if (!TextUtils.isEmpty(InfoManager.getUserInfoFile())) {
//            PluginEngineUtil.initUserInfo(InfoManager.getUserInfoFile());
//        }

        PluginEngine.getInstance().registerReceiver((code, message, extras) -> {
            if (code == PluginEngine.CODE_GET_SAVE_INFO) {
                Log.d("pluginUserInfo", "开始从本地抓取用户信息数据...");
                SaveInfoEntity saveInfoEntity = GsonUtils.fromJson(message, SaveInfoEntity.class);
                if (saveInfoEntity == null || saveInfoEntity.getData() == null)
                    return;

                Log.d("pluginUserInfo", "name: " + saveInfoEntity.getData().getName());
                Log.d("pluginUserInfo", "ComConstant: " + ComConstant.FILE_FAMILY_REGISTER);
                HashMap<String, String> hashMap = saveInfoEntity.getData().getValues();
                if (hashMap == null || hashMap.size() == 0)
                    return;

                if (saveInfoEntity.getData().getName().equals(ComConstant.FILE_FAMILY_REGISTER)) {
                    UserInfoEntity userInfoEntity = new UserInfoEntity();

                    for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        Log.d("pluginUserInfo", entry.getValue());
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
                Log.d("pluginUserInfo", "更新完毕用户信息...");
            } else if (code == PluginEngine.CODE_SAVE_INFO) {
                Log.d("pluginUserInfo", "用户设置保存文件名为: " + (TextUtils.isEmpty(message) ? "没有文件" : message));

                if (TextUtils.isEmpty(message))
                    return;

                InfoManager.saveUserInfoFile(message);
                PluginEngineUtil.initUserInfo(message);
            }
        });
    }
}
