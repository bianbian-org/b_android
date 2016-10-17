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

    public static KeyValuePair getAnnouncements(String user_id, String family_id, String ticket, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("family_id", family_id)
                .put("ticket", ticket)
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

    public static KeyValuePair submitComplaint(String user_id, String ticket, String types, String content, String mobile) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("types", types)
                .put("content", content)
                .put("mobile", mobile);
    }

    public static KeyValuePair getRepairs(String user_id, String ticket, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair submitRepair(String user_id, String ticket, String family_id, String repair_type, String repair_device, String note, String mobile) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id)
                .put("repair_type", repair_type)
                .put("repair_device", repair_device)
                .put("note", note)
                .put("mobile", mobile);
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

    public static KeyValuePair getRepairDetail(String user_id, String ticket, String repair_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("repair_id", repair_id);
    }

    public static KeyValuePair replyRepair(String user_id, String ticket, String content, String repair_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("content", content)
                .put("repair_id", repair_id);
    }

    public static KeyValuePair submitOnline(String family_id, String device_id) {
        return newPair()
                .put("family_id", family_id)
                .put("device_id", device_id);
    }

    public static KeyValuePair submitTimer(String family_id, String timer) {
        return newPair()
                .put("family_id", family_id)
                .put("json", timer);
    }
}
