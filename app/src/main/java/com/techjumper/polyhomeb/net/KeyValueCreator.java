package com.techjumper.polyhomeb.net;

import com.techjumper.lib2.others.KeyValuePair;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class KeyValueCreator {

    private static KeyValuePair<String, Object> newPair() {
        return new KeyValuePair<>();
    }

    public static KeyValuePair regist(String mobile, String sms_captcha, String password) {
        return newPair()
                .put("mobile", mobile)
                .put("sms_captcha", sms_captcha)
                .put("password", password);
    }

    public static KeyValuePair sendVerificationCode(String mobile, String type) {
        return newPair()
                .put("mobile", mobile)
                .put("type", type);
    }

    public static KeyValuePair login(String mobile, String password) {
        return newPair()
                .put("mobile", mobile)
                .put("password", password);
    }

    public static KeyValuePair findPassword(String mobile, String sms_captcha, String password) {
        return newPair()
                .put("mobile", mobile)
                .put("sms_captcha", sms_captcha)
                .put("new_password", password);
    }
}
