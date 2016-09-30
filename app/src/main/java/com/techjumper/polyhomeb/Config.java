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
    public static final boolean DEBUG = true;

    /**
     * polyhome域名
     */
    public static String sHost = "http://poly.techjumper.com";

    /**
     * polyhome接口地址
     * "http://poly.techjumper.com"
     * 默认接口地址
     */
    public static String sBaseUrl = sHost + "/api/v1bm/";

    /**
     * 友邻和商城链接
     */
    public static String sFriend = "http://pl.techjumper.com/neighbor?title=友邻&left=::NativeMenu&right=::NativeNewArticle,::NativeNotification";
    public static String sShopping = "http://pl.techjumper.com/shop/mobile?title=商城&left=::NativeMenu&right=::person";
    public static String sFriendNotification = "http://pl.techjumper.com/neighbor/user/notices?title=通知&left=::NativeReturn&right=";
    public static String sService = "http://pl.techjumper.com/neighbor/user/notices?title=通知&left=::NativeReturn&right=";

    /**
     * 在SD空间的文件夹名字,以及文件夹内部的头像文件夹名称以及log文件夹名称
     */
    public static final String sParentDirName = "polyhomeb";
    public static final String sAvatarsDirName = "avatars";
    public static final String sLogDirName = "log";

    /**
     * 医疗域名
     */
    public static String sMedicalHost = "https://182.92.80.103/";

    /**
     * 医疗接口地址
     */
    public static String sMedicalUrl = sMedicalHost;

    /**
     * 默认数据库的版本
     */
    public static final int DEFAULT_DB_VERSION = 1;




}
