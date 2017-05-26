package com.techjumper.polyhomeb.net;


import com.techjumper.polyhomeb.Config;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public enum PolyTcpUdpConfig {
    INSTANCE;

    public static String sCloudServer;

    static {
//        sCloudServer = Config.DEBUG ? "120.24.94.107" : "101.201.76.220";
        sCloudServer = Config.DEBUG ? "120.24.94.107" : "tcp.techjumper.cn";
    }

    /**
     * 超时时间
     */
    public static int sLocalTcpPort = 8625;

    public static int sCloudsPort = 6654;

    public static int sUdpServerPort = 8888;

}
