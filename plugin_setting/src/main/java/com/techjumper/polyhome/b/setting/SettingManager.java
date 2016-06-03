package com.techjumper.polyhome.b.setting;

import com.techjumper.corelib.utils.file.PreferenceUtils;

/**
 * 保存本地设置的
 * Created by kevin on 16/6/3.
 */
public enum SettingManager {

    INSTANCE;

    public static final String KEY_PASSWORD = "key_password"; //工程密码
    public static final String KEY_NETWORK_IS_ON = "key_network_is_on"; //是否启用网络设置
    public static final String KEY_SIP_IS_ON = "key_sip_is_on"; //是否启用sip设置


    private void savePassword(String password) {
        PreferenceUtils.save(KEY_PASSWORD, password);
    }

    /**
     * 是否匹配正确的工程密码
     *
     * @param password
     * @return
     */
    public boolean isPassword(String password) {
        if (password.equals(PreferenceUtils.get(KEY_PASSWORD, Config.defaultPassword))) {
            savePassword(password);
            return true;
        }
        return false;
    }

    /**
     * 判断网络设置开启状态
     *
     * @return
     */
    public boolean isOnNetWork() {
        return PreferenceUtils.get(KEY_NETWORK_IS_ON, false);
    }

    /**
     * 设置网络设置状态
     *
     * @param isOn
     */
    public void setOnNetWork(boolean isOn) {
        PreferenceUtils.save(KEY_NETWORK_IS_ON, isOn);
    }

    /**
     * 判断sip设置开启状态
     *
     * @return
     */
    public boolean isOnSip() {
        return PreferenceUtils.get(KEY_SIP_IS_ON, false);
    }

    public void setOnSip(boolean isOn) {
        PreferenceUtils.save(KEY_NETWORK_IS_ON, isOn);
    }
}
