package com.techjumper.polyhomeb;

import android.os.Environment;

import java.io.File;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Config {

    /**
     * 接口地址
     */
    public static String sHost;

    /**
     * 是否是调试环境
     */
    public static final boolean DEBUG = false;

    public static String HTTP;

    static {
        HTTP = DEBUG ? "http" : "https";
    }

    /**
     * polyhome域名
     */
    static {
        sHost = DEBUG ? "http://test.poly.ourjujia.com" : "https://service.polyhome.com";
    }

    public static final String AD_URL = "?title=&left=::NativeReturn&right=%E8%B4%AD%E7%89%A9%E8%BD%A6::shop_cart";

    /**
     * polyhome接口地址
     * "http://poly.techjumper.com"
     * 默认接口地址
     */
    public static String sBaseUrl = sHost + "/api/v1bm/";

    /**
     * C端接口地址
     * http://poly.techjumper.com/api/v1/
     */
    public static String sCBaseUrl = sHost + "/api/v1/";

    /**
     * 友邻和商城链接
     */
    public static String sFriend = sHost + "/neighbor?title=友邻&left=::NativeMenu&right=::NativeNewArticle,::NativeNotification";
    public static String sShopping = sHost + "/shop/mobile?title=商城&left=::NativeMenu&right=::person";
    public static String sFriendNotification = sHost + "/neighbor/user/notices?title=通知&left=::NativeReturn&right=";
    public static String sService = sHost + "/life?title=生活服务&left=::NativeMenu&right=";
    public static String sAboutUs = sHost + "/content/sp/bphone_about?title=关于我们&left=::NativeReturn&right=";
    public static String sUserAgreement = sHost + "/content/sp/bphone_useragreement?title=用户协议&left=::NativeReturn&right=";

    /**
     * 在SD空间的文件夹名字,以及文件夹内部的头像文件夹名称以及log文件夹名称
     */
    public static final String sParentDirName = "polyhomeb";
    public static final String sAPKDirName = "apk";
    public static final String sAvatarsDirName = ".avatars";
    public static final String sLogDirName = "log";
    public static final String sADVideoDirName = ".ad_video";

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

    /**
     * apk更新之后保存位置
     */
    public static final String sUpdate_Apk_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + Config.sParentDirName + File.separator
            + Config.sAPKDirName;

    /**
     * apk下载的文件名
     */
    public static final String sAPK_NAME = "temp.apk";

}
