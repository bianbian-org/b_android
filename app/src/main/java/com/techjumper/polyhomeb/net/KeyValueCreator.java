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

    public static KeyValuePair propertyNotice(String user_id, String family_id, String ticket, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("family_id", family_id)
                .put("ticket", ticket)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair propertyNoticeDetail(String user_id, String ticket, String noticeId) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("id", noticeId);
    }

    public static KeyValuePair propertyComplain(String user_id, String ticket, String status, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("status", status)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair propertyRepair(String user_id, String ticket, String status, String page, String count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("status", status)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair newComplain(String user_id, String ticket, String mobile, String types, String content, String[] imgs) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("mobile", mobile)
                .put("types", types)
                .put("content", content)
                .put("imgs", imgs);
    }

    public static KeyValuePair newRepair(String user_id, String ticket, String family_id, String mobile, String repair_type, String repair_device, String note, String[] imgs) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id)
                .put("mobile", mobile)
                .put("repair_type", repair_type)
                .put("repair_device", repair_device)
                .put("note", note)
                .put("imgs", imgs);
    }

    public static KeyValuePair uploadPic(String user_id, String ticket, String file) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("file", file);
    }

    public static KeyValuePair getComplainDetail(String user_id, String ticket, int id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("id", id);
    }

    public static KeyValuePair complainDetailReply(String user_id, String ticket, String content, String suggestion_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("content", content)
                .put("suggestion_id", suggestion_id);
    }

    public static KeyValuePair getRepairDetail(String user_id, String ticket, int repair_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("repair_id", repair_id);
    }

    public static KeyValuePair repairDetailReply(String user_id, String ticket, String content, String repair_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("content", content)
                .put("repair_id", repair_id);
    }
}
