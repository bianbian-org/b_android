package com.techjumper.polyhome.net;

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

    /**
     * 构造tcp的参数
     */
    public static KeyValuePair generateParamMethod(Object param, String method) {
        return newPair()
                .put("param", param)
                .put("method", method);
    }

    public static KeyValuePair empty() {
        return newPair();
    }

    public static KeyValuePair login(String mobile, String password) {
        return newPair()
                .put("mobile", mobile)
                .put("password", password);
    }

    public static KeyValuePair changePassword(String user_id, String ticket, String password, String new_password, String new_password_check) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("password", password)
                .put("new_password", new_password)
                .put("new_password_check", new_password_check);
    }

    public static KeyValuePair updateUserInfo(String user_id, String ticket, String username, String sex, String birthday, String email) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("username", username)
                .put("sex", sex)
                .put("birthday", birthday)
                .put("email", email);
    }
}
