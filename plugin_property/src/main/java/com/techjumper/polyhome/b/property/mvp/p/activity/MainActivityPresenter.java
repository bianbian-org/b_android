package com.techjumper.polyhome.b.property.mvp.p.activity;

import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.SchedulersCompat;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/12.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    private int backType = BackEvent.PROPERTY_ACTION;

    @OnClick(R.id.bottom_back)
    void back() {
        if (backType == BackEvent.PROPERTY_ACTION) {
            RxBus.INSTANCE.send(new PropertyActionEvent(false));
        } else if (backType == BackEvent.PROPERTY_LIST) {
            RxBus.INSTANCE.send(new PropertyListEvent());
        } else {
            getView().finish();
        }
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
        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof BackEvent) {
                        BackEvent backEvent = (BackEvent) o;
                        backType = backEvent.getType();
                    }
                }));

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
