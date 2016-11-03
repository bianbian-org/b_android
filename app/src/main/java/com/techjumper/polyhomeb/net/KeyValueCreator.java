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

    public static KeyValuePair replyComment(String user_id, String ticket, String forum_article_id, String comment_id, String content, String comment_image) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("forum_article_id", forum_article_id)
                .put("comment_id", comment_id)
                .put("content", content)
                .put("comment_image", comment_image);
    }

    public static KeyValuePair getSections(String user_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket);
    }

    /**
     * 新建帖子
     *
     * @param user_id          # 用户ID
     * @param ticket           # session登录验证
     * @param village_id       # 小区ID
     * @param forum_section_id # 版块ID
     * @param title            # 帖子标题
     * @param content          # 帖子内容
     * @param article_images   # 帖子图片
     * @param category         # 帖子类型 0-非闲置 1-闲置
     * @param price            # 现价
     * @param origin_price     # 原价
     * @param discount         # 是否接受议价 0-接受 1-不接受
     */
    public static KeyValuePair newArticle(String user_id, String ticket, String village_id, String forum_section_id, String title, String content, String[] article_images, String category, String price, String origin_price, String discount) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("village_id", village_id)
                .put("forum_section_id", forum_section_id)
                .put("title", title)
                .put("content", content)
                .put("article_images", article_images)
                .put("category", category)
                .put("price", price)
                .put("origin_price", origin_price)
                .put("discount", discount);
    }

    /**
     * 新建闲置
     *
     * @param user_id        # 用户ID
     * @param ticket         # session登录验证
     * @param village_id     # 小区ID
     * @param title          # 帖子标题
     * @param content        # 帖子内容
     * @param article_images #帖子图片
     * @param category       # 帖子类型 0-非闲置 1-闲置
     * @param price          # 现价
     * @param origin_price   # 原价
     * @param discount       # 是否接受议价 0-接受 1-不接受
     */
    public static KeyValuePair newArticle(String user_id, String ticket, String village_id, String title, String content, String[] article_images, String category, String price, String origin_price, String discount) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("village_id", village_id)
                .put("title", title)
                .put("content", content)
                .put("article_images", article_images)
                .put("category", category)
                .put("price", price)
                .put("origin_price", origin_price)
                .put("discount", discount);
    }

    public static KeyValuePair getVillages(String user_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket);
    }

    public static KeyValuePair joinVillage(String user_id, String ticket, int village_id, String building, String unit, String index) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("village_id", village_id)
                .put("building", building)
                .put("unit", unit)
                .put("index", index);
    }

    public static KeyValuePair getFamilyAndVillage(String user_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket);
    }

    public static KeyValuePair getMessages(String user_id, String ticket, String type, int page, int count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("type", type)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair setUserInfo(String user_id, String ticket, String username, String sex, String birthday, String email) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("username", username)
                .put("sex", sex)
                .put("birthday", birthday)
                .put("email", email);
    }

    public static KeyValuePair updateAvatar(String user_id, String ticket, String cover) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("cover", cover);
    }

    public static KeyValuePair getOrdersInfo(String user_id, String ticket, String family_id, String village_id, String status, String pay_type, int page, int count) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id)
                .put("village_id", village_id)
                .put("status", status)
                .put("pay_type", pay_type)
                .put("page", page)
                .put("count", count);
    }

    public static KeyValuePair checkIn(String user_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket);
    }

    public static KeyValuePair getCheckInData(String user_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket);
    }

    public static KeyValuePair medicalUserLogin(int devicetype, int logintype) {
        return newPair()
                .put("devicetype", devicetype)
                .put("logintype", logintype);
    }

    public static KeyValuePair payments(String user_ip, String user_id, String ticket, String category, String order_number) {
        return newPair()
                .put("user_ip", user_ip)
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("category", category)
                .put("order_number", order_number);
    }

    public static KeyValuePair queryFamily(String user_id, String ticket, String family_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id);
    }

    public static KeyValuePair getBLEDoorInfo(String user_id, String ticket, String village_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("village_id", village_id);
    }

    public static KeyValuePair getAppUpdateInfo(int platform, String[] packages) {
        return newPair()
                .put("platform", platform)
                .put("packages", packages);
    }

    public static KeyValuePair deductionWhenCall(String user_id, String ticket, String store_id, String user_tel, String shop_service_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("store_id", store_id)
                .put("user_tel", user_tel)
                .put("shop_service_id", shop_service_id);
    }

    public static KeyValuePair deleteArticle(String user_id, String ticket, String article_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("article_id", article_id);
    }
}
