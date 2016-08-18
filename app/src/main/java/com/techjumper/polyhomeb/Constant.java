package com.techjumper.polyhomeb;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Constant {

    public static final String KEY_CURRENT_BUTTON = "complaint";
    public static final int VALUE_COMPLAINT = 2;
    public static final int VALUE_REPAIR = 1;
    public static final int VALUE_PLACARD = 0;

    public static final String CURRENT_PIC_URL = "current_pic_position";
    public static final String ALL_PIC_URL = "all_pic_url";

    public static final String PLACARD_DETAIL_ID = "placard_detail_id";
    public static final String PLACARD_DETAIL_TITLE = "placard_detail_title";
    public static final String PLACARD_DETAIL_TYPE = "placard_detail_type";
    public static final String PLACARD_DETAIL_CONTENT = "placard_detail_content";
    public static final String PLACARD_DETAIL_TIME = "placard_detail_time";

    public static final String TRUE_ENTITY_RESULT = "true";

    public static final String PROPERTY_COMPLAIN_DATA_ID = "property_complain_data_id";
    public static final String PROPERTY_REPAIR_DATA_ID = "property_repair_data_id";

    public static final String PROPERTY_COMPLAIN_STATUS = "property_complain_status";
    public static final String PROPERTY_REPAIR_STATUS = "property_repair_status";
    public static final int STATUS_ALL = -1;  //全部
    public static final int STATUS_NOT_PROCESS = 0; //未处理
    public static final int STATUS_REPLY = 1;   //已回复
    public static final int STATUS_PROCESSED = 2;  //已处理
    public static final int STATUS_CLOSED = 3; //已关闭

    public static final int MESSAGE_SENDING = 0;  //发送中
    public static final int MESSAGE_SEND_FAILED = 1;  //发送失败
    public static final int MESSAGE_SEND_SUCCESS = 2;  //发送成功


    /* ------------------JS -------------------*/

    /**
     * className
     */
    public static final String JS_REPLY_ARTICLE = "article";
    public static final String JS_NATIVE_BRIDGE = "NativeBridge";

    /**
     *
     */
    public static final String JS_REPLY_ARTICLE_ID = "article_id";
    public static final String JS_REPLY_COMMENT_ID = "comment_id";
    public static final String JS_PAGE_JUMP_URL = "page_jump_url";



}
