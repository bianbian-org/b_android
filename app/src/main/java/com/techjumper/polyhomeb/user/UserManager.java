package com.techjumper.polyhomeb.user;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalAllUserEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.manager.PolyPluginFileManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;
import com.techjumper.polyhomeb.utils.HostIpHelper;

import java.util.List;

import okio.Buffer;

import static com.techjumper.corelib.utils.file.FileUtils.loadTextFile;

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

    public static final String VALUE_IS_FAMILY = "value_is_family";
    public static final String VALUE_IS_VILLAGE = "value_is_village";

    public static final String KEY_IS_CURRENT_COMMUNITY_SUPPORT_BLE_DOOR = "is_current_community_support_ble_door";
    public static final String KEY_CURRENT_COMMUNITY_BLE_DOOR_INFO = "current_community_ble_door_info";

    private static final String PATH = Utils.appContext.getFilesDir().getAbsolutePath() + "_" + "userinfo";

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
            //将所有的家庭信息存储到内部存储
            FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(
                    entity.getData().getFamilies())).inputStream(), PATH, KEY_ALL_FAMILIES);
            String family_id = entity.getData().getFamilies().get(0).getFamily_id();
            String family_name = entity.getData().getFamilies().get(0).getFamily_name();
            int village_id = entity.getData().getFamilies().get(0).getVillage_id();
            updateFamilyOrVillageInfo(true, family_id + "", family_name, village_id + "");
        }
        if (entity.getData().getVillages() != null && entity.getData().getVillages().size() != 0) {
            //登录接口多出来的
            //将所有的小区信息存储到内部存储
            FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(
                    entity.getData().getVillages())).inputStream(), PATH, KEY_ALL_VILLAGES);
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
     * 侧边栏存储请求到的家庭和小区数据
     */
    public void saveFamiliesAndVillages(UserFamiliesAndVillagesEntity entity) {
        FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(
                entity.getData().getFamily_infos())).inputStream(), PATH, KEY_ALL_FAMILIES);
        FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(
                entity.getData().getVillage_infos())).inputStream(), PATH, KEY_ALL_VILLAGES);
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
            PreferenceUtils.save(UserManager.KEY_CURRENT_FAMILY_ID, family_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_NAME, name);
            PreferenceUtils.save(UserManager.KEY_CURRENT_VILLAGE_ID, village_id);
            PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, UserManager.VALUE_IS_VILLAGE);
        }
    }

    /**
     * 得到用户所有家庭
     *
     * @return json字符串
     */
    public String getUserAllFamilies() {
        return loadTextFile(PATH, KEY_ALL_FAMILIES);
    }

    /**
     * 得到用户所有小区
     *
     * @return json字符串
     */
    public String getUserAllVillages() {
        return loadTextFile(PATH, KEY_ALL_VILLAGES);
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
     * 存储当前小区/家庭的蓝牙门锁信息
     */
    public void saveBLEInfo(BluetoothLockDoorInfoEntity entity) {
        if (entity.getData().getInfos() != null
                && entity.getData().getInfos().size() != 0) {
            List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infos = entity.getData().getInfos();
            FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(infos)).inputStream(),
                    PATH, KEY_CURRENT_COMMUNITY_BLE_DOOR_INFO);
        } else {
            FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(null)).inputStream(),
                    PATH, KEY_CURRENT_COMMUNITY_BLE_DOOR_INFO);
        }
        if (1 == entity.getData().getHas_bluelock()) {
            PreferenceUtils.save(KEY_IS_CURRENT_COMMUNITY_SUPPORT_BLE_DOOR, "1");
        } else {
            PreferenceUtils.save(KEY_IS_CURRENT_COMMUNITY_SUPPORT_BLE_DOOR, "0");
        }

    }

    /**
     * 得到当前小区/家庭的蓝牙门锁信息
     */
    public List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> getBLEInfo() {
        Gson gson = new Gson();
        String userInfo = FileUtils.loadTextFile(PATH, KEY_CURRENT_COMMUNITY_BLE_DOOR_INFO);
        if (TextUtils.isEmpty(userInfo)) return null; //在saveBLEInfo()方法中存入了空字符串
        List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> options
                = gson.fromJson(userInfo, new TypeToken<List<BluetoothLockDoorInfoEntity.DataBean.InfosBean>>() {
        }.getType());
        if (options == null || options.size() == 0) return null;
        return options;
    }

    /**
     * 当前小区/家庭是否支持蓝牙门锁
     */
    public boolean isCurrentCommunitySupportBLEDoor() {
        return PreferenceUtils.get(KEY_IS_CURRENT_COMMUNITY_SUPPORT_BLE_DOOR, "0").equals("1");
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
        PreferenceUtils.save(KEY_CURRENT_BUILDING, "");
        PreferenceUtils.save(KEY_CURRENT_UNIT, "");
        PreferenceUtils.save(KEY_CURRENT_ROOM, "");
        PreferenceUtils.save(KEY_CURRENT_SHOW_TITLE_NAME, "");
        PreferenceUtils.save(KEY_CURRENT_FAMILY_ID, "");
        PreferenceUtils.save(KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, "");
        PreferenceUtils.save(KEY_CURRENT_VILLAGE_ID, "");
        PreferenceUtils.save(KEY_IS_CURRENT_COMMUNITY_SUPPORT_BLE_DOOR, "0");
        PolyPluginFileManager.getInstance().clearFamilyInfoFile().subscribe();
        clearUserInfo();
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

    /**
     * 将内部存储的数据清空
     */
    public void clearUserInfo() {
        FileUtils.deleteDir(PATH);
    }

    /**
     * 当前小区或者家庭是否有权限进行 发帖，缴费，回复帖子等操作？
     * <p>如果当前是家庭，默认有权限</p>
     * <p>如果当前是小区，那么得到所有小区的数据，再进行判断：如果当前小区已经有通过审核的房间，那么有权限
     * ，如果当前小区所有的房间均没有通过审核，则无权限</p>
     */
    public boolean hasAuthority() {
        if (isFamily()) return true;
        Gson gson = new Gson();
        String userAllVillages = getUserAllVillages();
        if (TextUtils.isEmpty(userAllVillages)) return false;
        List<LoginEntity.LoginDataEntity.VillagesBean> options
                = gson.fromJson(userAllVillages, new TypeToken<List<LoginEntity.LoginDataEntity.VillagesBean>>() {
        }.getType());
        if (options == null || options.size() == 0) return false;
        String currentId = getCurrentId();

        boolean hasAuthority = false;
        for (LoginEntity.LoginDataEntity.VillagesBean bean : options) {
            int village_id = bean.getVillage_id();
            if ((village_id + "").equals(currentId)) {
                //说明就是当前这个选择的小区
                //然后取出本小区下挂的房间
                //遍历所有房间，如果有审核过的，那就返回true，如果全都是未审核，则返回false
                List<LoginEntity.LoginDataEntity.VillagesBean.RoomsBean> rooms = bean.getRooms();
                for (LoginEntity.LoginDataEntity.VillagesBean.RoomsBean roomBean : rooms) {
                    int verified = roomBean.getVerified();
                    if (verified == 1) {  //未审核
                        hasAuthority = true;
                        break;
                    }
                }
                break;
            }
        }
        return hasAuthority;
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
        FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(entities)).inputStream()
                , PATH, KEY_MEDICAL_ALL_USER_INFO_LIST);
    }

    public List<MedicalAllUserEntity> getMedicalAllUserInfo() {
        Gson gson = new Gson();
        String userInfo = FileUtils.loadTextFile(PATH, KEY_MEDICAL_ALL_USER_INFO_LIST);
        List<MedicalAllUserEntity> options
                = gson.fromJson(userInfo, new TypeToken<List<MedicalAllUserEntity>>() {
        }.getType());
        return options;
    }


}
