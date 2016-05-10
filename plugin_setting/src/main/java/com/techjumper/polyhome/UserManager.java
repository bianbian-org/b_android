package com.techjumper.polyhome;

import android.text.TextUtils;

import com.techjumper.commonres.entity.LoginEntity;
import com.techjumper.commonres.entity.event.LoginEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.file.PreferenceUtils;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

/**
 * 用户管理类
 */
public enum UserManager {

    INSTANCE;

    public static final String KEY_ID = "key_id";
    public static final String KEY_PHONE_NUMBER = "key_phone_number";
    public static final String KEY_TICKET = "key_ticket";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_AVATAR = "key_avatar";

    /**
     * 通过LoginEntity将用户信息同步到本地
     */
    public void saveUserInfo(LoginEntity entity) {
        LoginEntity.LoginDataEntity dataEntity = entity.getData();
        PreferenceUtils.save(KEY_ID, dataEntity.getId());
        PreferenceUtils.save(KEY_PHONE_NUMBER, dataEntity.getMobile());
        updateTicket(dataEntity.getTicket());
        PreferenceUtils.save(KEY_USER_NAME, dataEntity.getUsername());
        PreferenceUtils.save(KEY_AVATAR, dataEntity.getCover());
    }

    public String getUserNickName() {
        String userName = PreferenceUtils.get(KEY_USER_NAME, "");
        return TextUtils.isEmpty(userName)
                ? PreferenceUtils.get(KEY_PHONE_NUMBER, "") : userName;
    }

    /**
     * 更新Ticket
     */
    public String updateTicket(String ticket) {
        PreferenceUtils.save(KEY_TICKET, ticket);
        return ticket;
    }

    /**
     * 得到Ticket
     */
    public String getTicket() {
        return getUserInfo(KEY_TICKET);
    }

    /**
     * 是否已经登录
     */
    public boolean isLogin() {
        String ticket = getUserInfo(KEY_TICKET);
        return !TextUtils.isEmpty(ticket);
    }

    /**
     * 注销登陆
     */
    public void logout() {
        logout(true);
    }

    private void logout(boolean notify) {
        PreferenceUtils.save(KEY_ID, "");
        PreferenceUtils.save(KEY_PHONE_NUMBER, "");
        PreferenceUtils.save(KEY_TICKET, "");
        PreferenceUtils.save(KEY_AVATAR, "");
        PreferenceUtils.save(KEY_USER_NAME, "");
//        setCurrentFamilyInfo("", "", "");
//        HostIpHelper.getInstance().clear();
//        DeviceDataManager.getInstance().clearDevice();
//        if (notify)
//            notifyLoginOrLogoutEvent(false);
    }

//    /**
//     * 注销登陆(不通知)
//     */
//    public void logoutDontNotify() {
//        logout(false);
//    }
//
    /**
     * 发送登陆和登出事件
     *
     * @param isLogin true:登陆  false:登出
     */
    public void notifyLoginOrLogoutEvent(boolean isLogin) {
        RxBus.INSTANCE.send(new LoginEvent(isLogin));
    }

    /**
     * 得到用户信息
     */
    public String getUserInfo(String key) {
        return PreferenceUtils.get(key, "");
    }

    /**
     * 存储用户信息
     */
    public void saveUserInfo(String key, String value) {
        PreferenceUtils.save(key, value);
    }

    /**
     * 得到用户当前家庭的ID或者name
     */
    public String getCurrentFamilyInfo(String key) {
        return getUserInfo(key);
    }

}
