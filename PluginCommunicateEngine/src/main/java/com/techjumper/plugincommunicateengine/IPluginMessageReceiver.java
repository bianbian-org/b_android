package com.techjumper.plugincommunicateengine;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IPluginMessageReceiver {
    void onPluginMessageReceive(int code, String message);
}
