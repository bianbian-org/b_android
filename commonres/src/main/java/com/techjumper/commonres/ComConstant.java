package com.techjumper.commonres;

import com.techjumper.polyhome_b.adlib.Config;

/**
 * Created by kevin on 16/5/18.
 */
public class ComConstant {

    public static final String defaultTicket = "7694942bc4ebeb7d53724897ae622810f6186e44";
    public static final String defaultUserId = "248";

    //开关
    public static boolean titleFinish = true;
    public static boolean titleUpdate = true;

    //每一页请求多少
    public static final String PAGESIZE = "10";

    //host url 的开关, true 为 debug模式, false 为 release模式
    private static final boolean isDebug = false;

    //保存本地数据的文件名
    public static final String FILE_FAMILY_REGISTER = "poly_b_family_register";
    public static final String FILE_MEDICAL = "personInfor";
    public static final String FILE_HEARTBEATTIME = "heartbeatTime";

    public static final String FILE_HEARTBEATTIME_TIME = "time";

//    public static final String HOST_DEBUG = "http://pl.techjumper.com";
//    public static final String HOST_RELEASE = "http://api.ourjujia.com";
//
//    public static final String HOST = isDebug ? HOST_DEBUG : HOST_RELEASE;

    public static final String BASE_DEBUG_B_URL = Config.sHost + "/api/v1b/";
    public static final String BASE_DEBUG_URL = Config.sHost + "/api/v1/";
}
