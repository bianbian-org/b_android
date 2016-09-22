package com.techjumper.polyhome.b.home;

import com.techjumper.commonres.ComConstant;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Config {

    /**
     * 默认数据库的版本
     */
    public static final int DEFAULT_DB_VERSION = 1;
    /**
     * 默认接口地址
     */
    public static String sBaseUrl = ComConstant.BASE_DEBUG_B_URL;

    /**
     * 插件列表的配置名字
     */
    public static final String PLUGIN_LIST_ASSET_NAME = "plugin_list";
    /**
     * 插件列表的配置路径
     */
    public static final String PLUGIN_LIST_ASSET_PATH = "config/" + PLUGIN_LIST_ASSET_NAME + ".json";

}
