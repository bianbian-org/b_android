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

    public static String sHost;

    static {
        sHost = DEBUG ? "http://poly.techjumper.com" : "http://api.ourjujia.com";
    }

    /**
     * 默认接口地址
     */
    public static String sBaseUrl = sHost + "/api/v1/";

    /**
     * 默认数据库的版本
     */
    public static final int DEFAULT_DB_VERSION = 1;


}
