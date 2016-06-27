package com.techjumper.polyhome.b.home.net;

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

    public static KeyValuePair getInfo(String user_id, String ticket, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair getWeatherInfo(String familyId) {
        return newPair()
                .put("family_id", familyId);
    }

    public static KeyValuePair getNotices(String user_id, String family_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("family_id", family_id)
                .put("ticket", ticket);
    }
}
