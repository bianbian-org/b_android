package com.techjumper.polyhomeb.user;

import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.user.event.LoginEvent;
import com.techjumper.polyhomeb.utils.HostIpHelper;

import java.util.List;

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
    public static final String KEY_DEVICE_ID = "key_device_id";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_AVATAR = "key_avatar";
    public static final String KEY_SEX = "key_sex";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_BIRTHDAY = "key_birthday";
    public static final String KEY_ALL_FAMILIES = "key_all_families";
    public static final String KEY_ALL_VILLAGES = "key_all_villages";
    public static final String KEY_CURRENT_VILLAGE_ID = "key_current_village_id";
    public static final String KEY_CURRENT_FAMILY_ID = "key_current_family_id";
    public static final String KEY_CURRENT_VILLAGE_NAME = "key_current_village_name";
    public static final String KEY_CURRENT_FAMILY_NAME = "key_current_family_name";
    public static final String KEY_CURRENT_BUILDING = "key_current_building";
    public static final String KEY_CURRENT_UNIT = "key_current_unit";
    public static final String KEY_CURRENT_ROOM = "key_current_room";


    /**
     * 通过LoginEntity将用户信息同步到本地
     */

    /**
     * user_id:412,family_id:463,village_id:5,ticket:
     *
     * @param entity
     */
    public void saveUserInfo(LoginEntity entity) {
        LoginEntity.LoginDataEntity dataEntity = entity.getData();
        if (!TextUtils.isEmpty(dataEntity.getMobile())) {  //登录接口没有返回mobile,注册接口才有
            PreferenceUtils.save(KEY_PHONE_NUMBER, dataEntity.getMobile());
        }
        if (!TextUtils.isEmpty(dataEntity.getSex())) {  //登录接口多出来的
            PreferenceUtils.save(KEY_SEX, dataEntity.getSex());
        }
        if (!TextUtils.isEmpty(dataEntity.getEmail())) {  //登录接口多出来的
            PreferenceUtils.save(KEY_EMAIL, dataEntity.getEmail());
        }
        if (!TextUtils.isEmpty(dataEntity.getBirthday())) {  //登录接口多出来的
            PreferenceUtils.save(KEY_BIRTHDAY, dataEntity.getBirthday());
        }

        /************************测试数据,写死的**************************/
        PreferenceUtils.save(KEY_CURRENT_FAMILY_ID, 463 + "");  //家庭id
        PreferenceUtils.save(KEY_CURRENT_VILLAGE_ID, 5 + "");   //小区id是5,name是怡美家园
        PreferenceUtils.save(KEY_CURRENT_VILLAGE_NAME, "呵呵");
        PreferenceUtils.save(KEY_CURRENT_FAMILY_NAME, "怡美家园");
        /************************测试数据,写死的**************************/

        if (entity.getData().getFamilies() != null && entity.getData().getFamilies().size() != 0) {
            //登录接口多出来的
            PreferenceUtils.save(KEY_ALL_FAMILIES, GsonUtils.toJson(entity.getData().getFamilies()));
        }
        if (entity.getData().getVillages() != null && entity.getData().getVillages().size() != 0) {
            //登录接口多出来的
            PreferenceUtils.save(KEY_ALL_VILLAGES, GsonUtils.toJson(entity.getData().getVillages()));
        }
        PreferenceUtils.save(KEY_ID, dataEntity.getId());
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
     * 得到用户所有家庭
     *
     * @param key
     * @return
     */
    public List<LoginEntity.LoginDataEntity.FamiliesBean> getUserAllFamilies(String key) {
        String allFamiliesJson = PreferenceUtils.get(KEY_ALL_FAMILIES, key);
        return GsonUtils.fromJson(allFamiliesJson, List.class);
    }

    /**
     * 得到用户所有小区
     */
    public List<LoginEntity.LoginDataEntity.VillagesBean> getUserAllVillages(String key) {
        String allVillagesJson = PreferenceUtils.get(KEY_ALL_VILLAGES, key);
        return GsonUtils.fromJson(allVillagesJson, List.class);
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
     * 是否选择过家庭
     */
    public boolean hasFamily() {
        String family = getUserInfo(KEY_CURRENT_FAMILY_ID);
        return !TextUtils.isEmpty(family);
    }

    /**
     * 是否选择过小区
     */
    public boolean hasVillage() {
        String village = getUserInfo(KEY_CURRENT_VILLAGE_ID);
        return !TextUtils.isEmpty(village);
    }

    /**
     * 得到当前选择的家庭名字
     */
    public String getCurrentChooseFamily() {
        return getUserInfo(KEY_CURRENT_FAMILY_NAME);
    }

    /**
     * 得到当前选择的小区名字
     */
    public String getCurrentChooseVillage() {
        return getUserInfo(KEY_CURRENT_VILLAGE_NAME);
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
        PreferenceUtils.save(KEY_SEX, "");
        PreferenceUtils.save(KEY_EMAIL, "");
        PreferenceUtils.save(KEY_BIRTHDAY, "");
        PreferenceUtils.save(KEY_PHONE_NUMBER, "");
        PreferenceUtils.save(KEY_CURRENT_VILLAGE_ID, "");
        PreferenceUtils.save(KEY_TICKET, "");
        PreferenceUtils.save(KEY_AVATAR, "");
        PreferenceUtils.save(KEY_USER_NAME, "");
        PreferenceUtils.save(KEY_CURRENT_FAMILY_ID, "");
        PreferenceUtils.save(KEY_ALL_FAMILIES, "");
        PreferenceUtils.save(KEY_CURRENT_VILLAGE_NAME, "");
        PreferenceUtils.save(KEY_CURRENT_FAMILY_NAME, "");
        PreferenceUtils.save(KEY_CURRENT_BUILDING, "");
        PreferenceUtils.save(KEY_CURRENT_UNIT, "");
        PreferenceUtils.save(KEY_CURRENT_ROOM, "");
        HostIpHelper.getInstance().clear();
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

}
