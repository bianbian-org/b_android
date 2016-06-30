package com.techjumper.polyhomeb.user;

import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.polyhomeb.user.event.LoginEvent;
import com.techjumper.polyhomeb.utils.HostIpHelper;

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
    public static final String KEY_CURRENT_FAMILY_ID = "key_current_family_id";
    public static final String KEY_CURRENT_FAMILY_MANAGER_ID = "key_current_family_manager_id";
    public static final String KEY_CURRENT_FAMILY_NAME = "key_current_family_name";
    public static final String KEY_DEVICE_ID = "key_device_id";
    public static final String KEY_HAS_BINDING = "key_has_binding";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_AVATAR = "key_avatar";

    public static final String KEY_LAST_FAMILY_PREFIX = "key_last_family_";
    public static final String TYPE_LAST_FAMILY_FAMILY_ID = "last_family_type_family_id";
    public static final String TYPE_LAST_FAMILY_FAMILY_NAME = "last_family_type_family_name";
    public static final String TYPE_LAST_FAMILY_FAMILY_MANAGER_ID = "last_family_type_family_manager_id";



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
        if (!isLogin()) return;
        PreferenceUtils.save(KEY_ID, "");
        PreferenceUtils.save(KEY_PHONE_NUMBER, "");
        PreferenceUtils.save(KEY_TICKET, "");
        PreferenceUtils.save(KEY_HAS_BINDING, "");
        PreferenceUtils.save(KEY_AVATAR, "");
        PreferenceUtils.save(KEY_USER_NAME, "");
        setCurrentFamilyInfo("", "", "");
        HostIpHelper.getInstance().clear();
//        DeviceDataManager.getInstance().clearDevice();
//        TcpClientExecutor.INSTANCE.quit();
//        RoomDataManager.getInstance().clear();
        if (notify)
            notifyLoginOrLogoutEvent(false);
    }

    /**
     * 注销登陆(不通知)
     */
    public void logoutDontNotify() {
        logout(false);
    }

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

    /**
     * 存储用户当前家庭的ID和name
     */
    public void setCurrentFamilyInfo(String family_id, String family_name, String managerId) {
        PreferenceUtils.save(KEY_CURRENT_FAMILY_ID, family_id);
        PreferenceUtils.save(KEY_CURRENT_FAMILY_NAME, family_name);
        PreferenceUtils.save(KEY_CURRENT_FAMILY_MANAGER_ID, managerId);
    }

    /**
     * 是否绑定主机
     */
    public boolean hasBinding() {
        return !(NumberUtil.convertToint(getUserInfo(UserManager.KEY_HAS_BINDING), 0) == 0);
    }

    /**
     * 是否是家庭的管理员
     */
    public boolean isAdmin() {
        if (!isLogin()) return false;
        String userId = UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID);
        return !TextUtils.isEmpty(userId)
                &&
                userId.equals(UserManager.INSTANCE
                        .getCurrentFamilyInfo(UserManager.KEY_CURRENT_FAMILY_MANAGER_ID));
    }

    public void saveLastFamilyForUser(String familyId, String familyName, String managerId) {
        PreferenceUtils.save(getLastFamilyKey(getUserInfo(KEY_ID), TYPE_LAST_FAMILY_FAMILY_ID), familyId);
        PreferenceUtils.save(getLastFamilyKey(getUserInfo(KEY_ID), TYPE_LAST_FAMILY_FAMILY_NAME), familyName);
        PreferenceUtils.save(getLastFamilyKey(getUserInfo(KEY_ID), TYPE_LAST_FAMILY_FAMILY_MANAGER_ID), managerId);
    }

    public String getLastFamilyIdByCurrentUser() {
        return PreferenceUtils.get(getLastFamilyKey(getUserInfo(KEY_ID), TYPE_LAST_FAMILY_FAMILY_ID), "");
    }

    public String getLastFamilyNameByCurrentUser() {
        return PreferenceUtils.get(getLastFamilyKey(getUserInfo(KEY_ID), TYPE_LAST_FAMILY_FAMILY_NAME), "");
    }

    public String getLastFamilyManagerIdByCurrentUser() {
        return PreferenceUtils.get(getLastFamilyKey(getUserInfo(KEY_ID), TYPE_LAST_FAMILY_FAMILY_MANAGER_ID), "");
    }

    private String getLastFamilyKey(String userId, String type) {
        return KEY_LAST_FAMILY_PREFIX + userId + "_" + type;
    }
}
