package com.techjumper.polyhomeb;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Constant {

    /**
     * 缴费的button其实不需要这个value.剩下的维修,投诉和公告才需要,这些字段是为了区别进入的物业Activity是从哪进入的,便于展示右上角的"+"和当前显示的fragment是第几个
     */
    public static final String KEY_CURRENT_BUTTON = "complaint";
    public static final int VALUE_COMPLAINT = 2;
    public static final int VALUE_REPAIR = 1;
    public static final int VALUE_PLACARD = 0;
    public static final int VALUE_PAYMENT = 3;  //可以不要这个.

    public static final String CURRENT_PIC_URL = "current_pic_position";
    public static final String ALL_PIC_URL = "all_pic_url";

    public static final String PLACARD_DETAIL_ID = "placard_detail_id";
    public static final String PLACARD_DETAIL_TITLE = "placard_detail_title";
    public static final String PLACARD_DETAIL_TYPE = "placard_detail_type";
    public static final String PLACARD_DETAIL_CONTENT = "placard_detail_content";
    public static final String PLACARD_DETAIL_TIME = "placard_detail_time";
    public static final String PLACARD_DETAIL_COME_FROM = "placard_detail_come_from";

    public static final String TRUE_ENTITY_RESULT = "true";
    public static final String FALSE_ENTITY_RESULT = "false";

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

    public static final String CHOOSE_VILLAGE_NAME = "choose_village_name";
    public static final String CHOOSE_VILLAGE_ID = "choose_village_id";

    public static final String KEY_COME_FROM = "comeFrom";
    public static final int VALUE_COME_FROM = 1;

    public static final int UNPAID_FRAGMENT_TITLE = 1;
    public static final int PAID_FRAGMENT_TITLE = 2;

    public static final String KEY_ORDER_NUMBER = "key_order_number";  //订单号  512841286429178
    public static final String KEY_PAY_NAME = "key_pay_name";    //费用名称  二月份燃气费
    public static final String KEY_PAY_TYPE = "key_pay_type";    //费用类型  水电,物业之类
    public static final String KEY_PAY_OBJECT = "key_pay_object";  //收费对象 4-2-4-1
    public static final String KEY_PAY_DEATH_LINE = "key_pay_death_line";   //截止日期  2016-6-6
    public static final String KEY_PAY_TOTAL = "key_pay_total";   //费用总计   $99
    public static final String KEY_PAY_IS_LATE = "key_pay_is_late";   //是否逾期
    public static final String KEY_PAY_DAY = "key_pay_day";   //超过X天,  还剩下10天,是+10天,逾期超过了10天则是-10天
    public static final String KEY_PAY_EXPIRY = "key_pay_expiry";  //滞纳金
    public static final String KEY_PAY_ALL_COST = "key_pay_all_cast";  //总金额,加上滞纳金的

    //支付方式
    public static final String PAYMENT_WAY = "payment_way";
    public static final int TENCENT_PAY = 1;
    public static final int ALIPAY = 2;
    public static final int UNION_PAY = 3;
    public static final int YI_PAY = 4;

    public static final String KEY_MESSAGE_ID = "key_message_id";


    /* ------------------JS -------------------*/

    /**
     * className
     */
    public static final String JS_NATIVE_BRIDGE = "NativeBridge";

    /**
     * jsConstant
     */
    public static final String JS_REPLY_ARTICLE_ID = "article_id";
    public static final String JS_REPLY_COMMENT_ID = "comment_id";
    public static final String JS_PAGE_JUMP_URL = "page_jump_url";

    /**
     * 查看H5大图
     */
    public static final String JS_BIG_PIC_INDEX = "js_big_pic_index";
    public static final String JS_BIG_PIC_ARRAY = "js_big_pic_array";

    /**
     * 聚家跳转网页
     */
    public static final String KEY_JUJIA_JUMP_URL = "key_jujia_jump_url";

}