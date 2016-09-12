package com.techjumper.polyhome.b.property;

import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.corelib.utils.file.PreferenceUtils;

/**
 * Created by kevin on 16/6/15.
 */
public enum UserInfoManager {

    INSTANCE;

    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_ID = "key_id";
    public static final String KEY_FAMILY_NAME = "key_family_name";
    public static final String KEY_TICKET = "key_ticket";
    public static final String KEY_HAS_BINDING = "key_has_binding";
    public static final String KEY_MOBILE = "key_mobile";

    /**
     * 保存用户
     *
     * @param entity
     */
    public static void saveUserInfo(UserInfoEntity entity) {
        PreferenceUtils.save(KEY_ID, entity.getId());
        PreferenceUtils.save(KEY_FAMILY_NAME, entity.getFamily_name());
        PreferenceUtils.save(KEY_USER_ID, entity.getUser_id());
        PreferenceUtils.save(KEY_TICKET, entity.getTicket());
        PreferenceUtils.save(KEY_HAS_BINDING, entity.getHas_binding());
    }

    /**
     * 获取userId
     *
     * @return
     */
    public static String getUserId() {
        return String.valueOf(PreferenceUtils.get(KEY_USER_ID, -1L));
    }

    /**
     * 获取家庭id
     *
     * @return
     */
    public static String getFamilyId() {
        return String.valueOf(PreferenceUtils.get(KEY_ID, -1L));
    }

    /**
     * 保存新的ticket
     * @param ticket
     */
    public static void saveTicket(String ticket) {
        PreferenceUtils.save(KEY_TICKET, ticket);
    }

    /**
     * 获取ticket
     *
     * @return
     */
    public static String getTicket() {
        return PreferenceUtils.get(KEY_TICKET, "");
    }

    public static void saveMobile(String mobile) {
        PreferenceUtils.save(KEY_MOBILE, mobile);
    }

    public static String getMobile() {
        return PreferenceUtils.get(KEY_MOBILE, "");
    }
}
