package com.techjumper.polyhomeb.user;

import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.Config;
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
    public static final String KEY_LOCAL_AVATAR = "key_local_avatar";
    public static final String KEY_SEX = "key_sex";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_BIRTHDAY = "key_birthday";
    public static final String KEY_ALL_FAMILIES = "key_all_families";
    public static final String KEY_ALL_VILLAGES = "key_all_villages";
    public static final String KEY_CURRENT_BUILDING = "key_current_building";
    public static final String KEY_CURRENT_UNIT = "key_current_unit";
    public static final String KEY_CURRENT_ROOM = "key_current_room";
    public static final String KEY_CURRENT_SHOW_TITLE_NAME = "key_current_show_title";
    public static final String KEY_CURRENT_FAMILY_ID = "key_current_show_id";
    public static final String KEY_CURRENT_VILLAGE_ID = "key_current_village_id";
    public static final String KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE = "key_current_show_is+family_or_village";

    public static final String VALUE_IS_FAMILY = "value_is_family";
    public static final String VALUE_IS_VILLAGE = "value_is_village";


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
            PreferenceUtils.save(KEY_PHONE_NUMBER, dataEntity.getMobile() + "");
        }
        if (!TextUtils.isEmpty(dataEntity.getSex())) {  //登录接口多出来的
            PreferenceUtils.save(KEY_SEX, dataEntity.getSex() + "");
        }
        if (!TextUtils.isEmpty(dataEntity.getEmail())) {  //登录接口多出来的
            PreferenceUtils.save(KEY_EMAIL, dataEntity.getEmail());
        }
        if (!TextUtils.isEmpty(dataEntity.getBirthday())) {  //登录接口多出来的
            PreferenceUtils.save(KEY_BIRTHDAY, dataEntity.getBirthday());
        }

        if (entity.getData().getFamilies() != null && entity.getData().getFamilies().size() != 0) {
            //登录接口多出来的
            PreferenceUtils.save(KEY_ALL_FAMILIES, GsonUtils.toJson(entity.getData().getFamilies()));
            String family_id = entity.getData().getFamilies().get(0).getFamily_id();
            String family_name = entity.getData().getFamilies().get(0).getFamily_name();
            int village_id = entity.getData().getFamilies().get(0).getVillage_id();
            updateFamilyOrVillageInfo(true, family_id, family_name, village_id);
        }
        if (entity.getData().getVillages() != null && entity.getData().getVillages().size() != 0) {
            //登录接口多出来的
            PreferenceUtils.save(KEY_ALL_VILLAGES, GsonUtils.toJson(entity.getData().getVillages()));
            //如果KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE是空的,或者value不是家庭的话,证明刚才没有存入家庭,现在就需要存小区.
            if (TextUtils.isEmpty(getUserInfo(KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE))
                    || !VALUE_IS_FAMILY.equals(getUserInfo(KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE))) {
                int village_id = entity.getData().getVillages().get(0).getVillage_id();
                String village_name = entity.getData().getVillages().get(0).getVillage_name();
                updateFamilyOrVillageInfo(false, village_id + "", village_name, village_id);
            }
        }
        PreferenceUtils.save(KEY_ID, dataEntity.getId());
        updateTicket(dataEntity.getTicket());
        PreferenceUtils.save(KEY_USER_NAME, dataEntity.getUsername());
        PreferenceUtils.save(KEY_AVATAR, Config.sHost + dataEntity.getCover());
    }

    /**
     * 更新Ticket
     */
    public String updateTicket(String ticket) {
        PreferenceUtils.save(KEY_TICKET, ticket);
        return ticket;
    }

    /**
     * 更新当前家庭和小区的信息
     * 如果不是家庭的话,KEY_CURRENT_FAMILY_ID存入的就是小区id.
     * <p>
     * <p>
     * 如果是家庭那么KEY_CURRENT_FAMILY_ID就是家庭id,KEY_CURRENT_VILLAGE_ID就是小区id
     * 如果是小区,那么KEY_CURRENT_FAMILY_ID和KEY_CURRENT_VILLAGE_ID就是小区id都是小区id
     * <p>
     * <p>
     * 所以使用之前,如果要取家庭的话,就必须要判断isFamily(),如果只是拿小区id的话,则直接拿就行,因为只要你能登陆app,那就说明至少都有小区id,不一定有家庭id
     */
    public void updateFamilyOrVillageInfo(boolean isFamily, String family_id, String name, int village_id) {
        if (isFamily) {
            PreferenceUtils.save(UserManager.KEY_CURRENT_FAMILY_ID, family_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_NAME, name);
            PreferenceUtils.save(UserManager.KEY_CURRENT_VILLAGE_ID, village_id + "");
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, UserManager.VALUE_IS_FAMILY);
        } else {
            PreferenceUtils.save(UserManager.KEY_CURRENT_FAMILY_ID, village_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_NAME, name);
            PreferenceUtils.save(UserManager.KEY_CURRENT_VILLAGE_ID, village_id + "");
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, UserManager.VALUE_IS_VILLAGE);
        }
    }

    /**
     * 得到用户所有家庭
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
     * 是否选择过家庭或者小区
     */
    public boolean hasChoosedFamilyOrVillage() {
        String name = getUserInfo(KEY_CURRENT_SHOW_TITLE_NAME);
        return !TextUtils.isEmpty(name);
    }

    /**
     * 当前正在使用的家庭或者小区的name
     */
    public String getCurrentTitle() {
        return getUserInfo(KEY_CURRENT_SHOW_TITLE_NAME);
    }

    /**
     * 当前正在使用的家庭或者小区的id
     */
    public String getCurrentId() {
        return getUserInfo(KEY_CURRENT_FAMILY_ID);
    }

    /**
     * 得到用户信息
     */
    public String getUserInfo(String key) {
        return PreferenceUtils.get(key, "");
    }

    /**
     * 当前是不是家庭 true为家庭,false为小区
     */
    public boolean isFamily() {
        String userInfo = getUserInfo(KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE);
        if (VALUE_IS_FAMILY.equals(userInfo)) {
            return true;
        } else {
            return false;
        }
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
        PreferenceUtils.save(KEY_TICKET, "");
        PreferenceUtils.save(KEY_AVATAR, "");
        PreferenceUtils.save(KEY_LOCAL_AVATAR, "");
        PreferenceUtils.save(KEY_USER_NAME, "");
        PreferenceUtils.save(KEY_ALL_FAMILIES, "");
        PreferenceUtils.save(KEY_ALL_VILLAGES, "");
        PreferenceUtils.save(KEY_CURRENT_BUILDING, "");
        PreferenceUtils.save(KEY_CURRENT_UNIT, "");
        PreferenceUtils.save(KEY_CURRENT_ROOM, "");
        PreferenceUtils.save(KEY_CURRENT_SHOW_TITLE_NAME, "");
        PreferenceUtils.save(KEY_CURRENT_FAMILY_ID, "");
        PreferenceUtils.save(KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, "");
        PreferenceUtils.save(KEY_CURRENT_VILLAGE_ID, "");

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
