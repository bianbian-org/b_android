package com.techjumper.polyhome.b.property.net;

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

    public static KeyValuePair getAnnouncements(String page, String count) {
        return newPair()
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair getComplaints(String user_id, String ticket, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair submitComplaint(String user_id, String ticket, String types, String content) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("types", types)
                .put("content", content);
    }

    public static KeyValuePair getRepairs(String user_id, String ticket, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair submitRepair(String user_id, String ticket, String family_id, String repair_type, String repair_device, String note) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id)
                .put("repair_type", repair_type)
                .put("repair_device", repair_device)
                .put("note", note);
    }

    public static KeyValuePair getComplaintDetail(String user_id, String ticket, String id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("id", id);
    }

    public static KeyValuePair replyComplaint(String user_id, String ticket, String content, String suggestion_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("content", content)
                .put("suggestion_id", suggestion_id);
    }
}
