package com.techjumper.polyhome_b.adlib.net;


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

    public static KeyValuePair padAd(String family_id, String user_id, String ticket) {
        return newPair()
                .put("family_id", family_id)
                .put("user_id", user_id)
                .put("ticket", ticket);
    }

    public static KeyValuePair adStat(String family_id, String json) {
        return newPair()
                .put("family_id", family_id)
                .put("json", json);
    }


}
