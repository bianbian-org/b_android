package com.techjumper.polyhomeb.user;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalAllUserEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
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
    public static final String KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE = "key_current_show_is_family_or_village";
    public static final String KEY_CURRENT_FAMILY_INFO = "key_current_family_info";

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
//            PreferenceUtils.save(KEY_ALL_FAMILIES, GsonUtils.toJson(entity.getData().getFamilies()));
            Gson gson = new Gson();
            String json = gson.toJson(entity.getData().getFamilies(), List.class);
            PreferenceUtils.save(KEY_ALL_FAMILIES, json);

            String family_id = entity.getData().getFamilies().get(0).getFamily_id();
            String family_name = entity.getData().getFamilies().get(0).getFamily_name();
            int village_id = entity.getData().getFamilies().get(0).getVillage_id();
            updateFamilyOrVillageInfo(true, family_id + "", family_name, village_id + "");
        }
        if (entity.getData().getVillages() != null && entity.getData().getVillages().size() != 0) {
            //登录接口多出来的
//            PreferenceUtils.save(KEY_ALL_VILLAGES, GsonUtils.toJson(entity.getData().getVillages()));
            Gson gson = new Gson();
            String json = gson.toJson(entity.getData().getVillages(), List.class);
            PreferenceUtils.save(KEY_ALL_VILLAGES, json);
            //如果KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE是空的,或者value不是家庭的话,证明刚才没有存入家庭,现在就需要存小区.
            if (TextUtils.isEmpty(getUserInfo(KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE))
                    || !VALUE_IS_FAMILY.equals(getUserInfo(KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE))) {
                int village_id = entity.getData().getVillages().get(0).getVillage_id();
                String village_name = entity.getData().getVillages().get(0).getVillage_name();
                updateFamilyOrVillageInfo(false, village_id + "", village_name, village_id + "");
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
    public void updateFamilyOrVillageInfo(boolean isFamily, String family_id, String name, String village_id) {
        if (isFamily) {
            PreferenceUtils.save(UserManager.KEY_CURRENT_FAMILY_ID, family_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_NAME, name);
            PreferenceUtils.save(UserManager.KEY_CURRENT_VILLAGE_ID, village_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, UserManager.VALUE_IS_FAMILY);
        } else {
//            PreferenceUtils.save(UserManager.KEY_CURRENT_FAMILY_ID, "");
            PreferenceUtils.save(UserManager.KEY_CURRENT_FAMILY_ID, family_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_NAME, name);
            PreferenceUtils.save(UserManager.KEY_CURRENT_VILLAGE_ID, village_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, UserManager.VALUE_IS_VILLAGE);
        }

        JLog.e("f" + getUserInfo(KEY_CURRENT_VILLAGE_ID));
        JLog.e("v" + getUserInfo(KEY_CURRENT_FAMILY_ID));
    }

    /**
     * 得到用户所有家庭
     */
    public List<LoginEntity.LoginDataEntity.FamiliesBean> getUserAllFamilies(String key) {
//        String allFamiliesJson = PreferenceUtils.get(KEY_ALL_FAMILIES, key);
//        return GsonUtils.fromJson(allFamiliesJson, List.class);
        Gson gson = new Gson();
        String userInfo = PreferenceUtils.get(KEY_ALL_FAMILIES, "");
        List<LoginEntity.LoginDataEntity.FamiliesBean> options = gson.fromJson(userInfo, new TypeToken<List<LoginEntity.LoginDataEntity.FamiliesBean>>() {
        }.getType());
        return options;
    }

    /**
     * 得到用户所有小区
     */
    public List<LoginEntity.LoginDataEntity.VillagesBean> getUserAllVillages(String key) {
//        String allVillagesJson = PreferenceUtils.get(KEY_ALL_VILLAGES, key);
//        return GsonUtils.fromJson(allVillagesJson, List.class);
        Gson gson = new Gson();
        String userInfo = PreferenceUtils.get(KEY_ALL_VILLAGES, "");
        List<LoginEntity.LoginDataEntity.VillagesBean> options = gson.fromJson(userInfo, new TypeToken<List<LoginEntity.LoginDataEntity.VillagesBean>>() {
        }.getType());
        return options;
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
        return VALUE_IS_FAMILY.equals(userInfo);
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


    /**************************************
     * 医疗的用户信息
     **********************************/
    public static final String KEY_MEDICAL_CURRENT_USER_ID = "key_medical_current_user_id";
    public static final String KEY_MEDICAL_CURRENT_USER_TOKEN = "key_medical_current_user_token";
    public static final String KEY_MEDICAL_CURRENT_USER_EMAIL = "key_medical_current_user_email";
    public static final String KEY_MEDICAL_CURRENT_USER_MOBILE_PHONE = "key_medical_current_user_mobile_phone";
    public static final String KEY_MEDICAL_CURRENT_USER_ID_CARD = "key_medical_current_user_id_card";
    public static final String KEY_MEDICAL_CURRENT_USER_HOME_PHONE = "key_medical_current_user_home_phone";
    public static final String KEY_MEDICAL_CURRENT_USER_P_NAME = "key_medical_current_user_p_name";
    public static final String KEY_MEDICAL_CURRENT_USER_SEX = "key_medical_current_user_sex";
    public static final String KEY_MEDICAL_CURRENT_USER_NICK_NAME = "key_medical_current_user_nick_name";
    public static final String KEY_MEDICAL_CURRENT_USER_BIRTHDAY = "key_medical_current_user_birthday";
    public static final String KEY_MEDICAL_CURRENT_USER_WEIGHT = "key_medical_current_user_weight";
    public static final String KEY_MEDICAL_CURRENT_USER_HEIGHT = "key_medical_current_user_height";
    public static final String KEY_MEDICAL_CURRENT_USER_ACCOUNT_ID = "key_medical_current_account_id";

    public static final String KEY_MEDICAL_ALL_USER_INFO_LIST = "key_medical_all_user_info_list";


    public void saveMedicalUserInfo(MedicalUserLoginEntity entity) {
        MedicalUserLoginEntity.MemberBean data = entity.getMember();
        String email = data.getEmail();  //邮箱
        String mobilePhone = data.getMobilePhone(); //手机号
        String idcard = data.getIdcard();  //身份证
        String homePhone = data.getHomePhone();  //座机
        String pname = data.getPname();  //姓名
        int sex = data.getSex();  //性别 : 1:男;2:女
        String nickname = data.getNickname();  //昵称
        String birthday = data.getBirthday();  //生日(格式yyyy-MM-dd)
        String weight = data.getWeight();  //体重(浮点数,公斤)
        String height = data.getHeight();   //身高(整数,厘米)
        String username = data.getUsername();  //账号 polyvip
        String id = data.getId();  //用户唯一标识id  1000000843764

        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_EMAIL, email);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_MOBILE_PHONE, mobilePhone);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_ID_CARD, idcard);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_HOME_PHONE, homePhone);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_P_NAME, pname);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_SEX, sex + "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_NICK_NAME, nickname);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_BIRTHDAY, birthday);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_WEIGHT, weight);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_HEIGHT, height);
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_ACCOUNT_ID, id);
    }

    public void medicalLogout() {
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_ID, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_TOKEN, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_P_NAME, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_EMAIL, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_MOBILE_PHONE, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_ID_CARD, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_HOME_PHONE, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_SEX, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_NICK_NAME, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_BIRTHDAY, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_WEIGHT, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_HEIGHT, "");
        PreferenceUtils.save(KEY_MEDICAL_CURRENT_USER_ACCOUNT_ID, "");
    }

    public void saveMedicalAllUserInfo(List<MedicalAllUserEntity> entities) {
        Gson gson = new Gson();
        String json = gson.toJson(entities, List.class);
        PreferenceUtils.save(KEY_MEDICAL_ALL_USER_INFO_LIST, json);
//        PreferenceUtils.save(KEY_MEDICAL_ALL_USER_INFO_LIST, GsonUtils.toJson(entities));
    }

    public List<MedicalAllUserEntity> getMedicalAllUserInfo() {
        Gson gson = new Gson();
        String userInfo = PreferenceUtils.get(KEY_MEDICAL_ALL_USER_INFO_LIST, "");
        List<MedicalAllUserEntity> options = gson.fromJson(userInfo, new TypeToken<List<MedicalAllUserEntity>>() {
        }.getType());
        return options;
//        String userInfo = PreferenceUtils.get(KEY_MEDICAL_ALL_USER_INFO_LIST, "");
//        return GsonUtils.fromJson(userInfo, List.class);
    }


}
