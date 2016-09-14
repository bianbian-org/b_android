package com.techjumper.polyhome_b.adlib;

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
    public static final boolean DEBUG = true;

    public static String sHost;
    public static String sJujia;
    public static String sShopping;
    public static String sShoppingLogin;
    public static String sShoppingShow;

    static {
        sHost = DEBUG ? "http://poly.techjumper.com" : "http://api.ourjujia.com";
    }

    static {
        sJujia = DEBUG ? "http://jujia.techjumper.com" : "http://www.ourjujia.com";
    }

    static {
        sShopping = DEBUG ? "http://pl.techjumper.com/shop/pad" : "http://polyhome.techjumper.com/shop/pad";
    }

    static {
        sShoppingLogin = sShopping + "/login";
    }

    static {
        sShoppingShow = sShopping + "/order/show/";
    }

    /**
     * 默认接口地址
     */
    public static String sBaseUrl = sHost + "/api/v1b/";

//    /**
//     * 默认数据库的版本
//     */
//    public static final int DEFAULT_DB_VERSION = 1;
}
