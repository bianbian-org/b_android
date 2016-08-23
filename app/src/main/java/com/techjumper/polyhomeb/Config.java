package com.techjumper.polyhomeb;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Config {


    /**
     * 是否是调试环境
     */
    public static final boolean DEBUG = false;

    public static String sHost = "http://poly.techjumper.com";

    public static String sFriend = "http://pl.techjumper.com/neighbor?title=友邻&left=::NativeMenu&right=::NativeNewArticle&refresh=refresh";
    public static String sShopping = "http://pl.techjumper.com/shop/mobile?title=商城&left=::NativeMenu&right=";
    public static String sFriendErrorPage = "file:///android_asset/404.html";

//    static {
//        sHost = DEBUG ? "http://poly.techjumper.com" : "http://api.ourjujia.com";
//    }

    /**
     * "http://poly.techjumper.com"
     * 默认接口地址
     */
    public static String sBaseUrl = sHost + "/api/v1bm/";

    /**
     * 默认数据库的版本
     */
    public static final int DEFAULT_DB_VERSION = 1;


}
