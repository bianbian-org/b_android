package com.techjumper.polyhome.b.home.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.home.UserInfoManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 16/7/5.
 */

public class SettingReceiver extends BroadcastReceiver {

    public static final String KEY_CODE = "key_code";
    public static final String KEY_EXTRA = "key_extra";
    public static final String KEY_MESSAGE = "key_msg";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null)
            return;

//        Log.d("pluginUserInfo", "开始抓取用户信息数据...");
//        Log.d("pluginUserInfo", "KEY_CODE" + intent.getIntExtra(KEY_CODE, -1) + "");
//
//        if (intent.getIntExtra(KEY_CODE, -1) == PluginEngine.CODE_GET_SAVE_INFO) {
//            Bundle bundle = intent.getBundleExtra(KEY_EXTRA);
//            String message = bundle.getString(KEY_MESSAGE);
//            Log.d("pluginUserInfo", "开始从本地抓取用户信息数据...");
//            SaveInfoEntity saveInfoEntity = GsonUtils.fromJson(message, SaveInfoEntity.class);
//            if (saveInfoEntity == null || saveInfoEntity.getData() == null)
//                return;
//
//            Log.d("pluginUserInfo", "name: " + saveInfoEntity.getData().getName());
//            HashMap<String, String> hashMap = saveInfoEntity.getData().getValues();
//            if (hashMap == null || hashMap.size() == 0)
//                return;
//
//            UserInfoEntity userInfoEntity = new UserInfoEntity();
//
//            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
//                Log.d("pluginUserInfo", entry.getValue());
//                String key = entry.getKey();
//                String value = entry.getValue();
//                if (key.equals("id")) {
//                    userInfoEntity.setId(Long.parseLong(value));
//                } else if (key.equals("family_name")) {
//                    userInfoEntity.setFamily_name(value);
//                } else if (key.equals("user_id")) {
//                    userInfoEntity.setUser_id(Long.parseLong(value));
//                } else if (key.equals("ticket")) {
//                    userInfoEntity.setTicket(value);
//                } else if (key.equals("has_binding")) {
//                    userInfoEntity.setHas_binding(Integer.parseInt(value));
//                }
//            }
//
//            UserInfoManager.saveUserInfo(userInfoEntity);
//
//            RxBus.INSTANCE.send(new UserInfoEvent(userInfoEntity));
//        }
    }
}
