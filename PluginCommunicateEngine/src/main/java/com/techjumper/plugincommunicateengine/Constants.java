package com.techjumper.plugincommunicateengine;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Constants {

    /**
     * 与插件通信的暗号
     */
    public static final int CODE_START_PLUGIN = 1;  //打开指定插件
    public static final int CODE_START_PLUGIN_ACTIVITY = 2; //打开指定插件的指定页面
    @Deprecated
    public static final int CODE_GET_PLUGIN_INFO = 3; //获取插件信息
}
