package com.techjumper.commonres.util;

import android.os.Bundle;

import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.plugincommunicateengine.HostDataBuilder;
import com.techjumper.plugincommunicateengine.IPluginMessageReceiver;
import com.techjumper.plugincommunicateengine.PluginEngine;

import static com.techjumper.commonres.ComConstant.FILE_MEDICAL;

/**
 * Created by kevin on 16/6/13.
 */
public class PluginEngineUtil {

    public static final String PLUGIN_PROPERTY = "com.techjumper.polyhome.b.property";
    public static final String PLUGIN_INFO = "com.techjumper.polyhome.b.info";
    public static final String PLUGIN_SMARTHOME = "com.polyhome.sceneanddevice";
    public static final String PLUGIN_SETTING = "com.techjumper.polyhome.b.setting";
    public static final String PLUGIN_MEDICAL = "pltk.com.medical";
    public static final String PLUGIN_TALK = "com.dnake.talk";

    public static final String NAME_USERINFO = "poly_b_family_register"; //获取用户登录后的信息文件名

    //物业
    public static void startProperty(final long familyId, final long userId, final String ticket) {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {

                Bundle bundle = new Bundle();
                bundle.putLong(PluginConstant.KEY_PRO_FAMILY_ID, familyId);
                bundle.putLong(PluginConstant.KEY_PRO_USER_ID, userId);
                bundle.putString(PluginConstant.KEY_PRO_TICKET, ticket);

                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_PROPERTY)
                        .build();
                try {
                    pluginExecutor.send(PluginEngine.CODE_START_PLUGIN, data, bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //信息
    public static void startInfo(final long userId, final long familyId, final String ticket, final int type) {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                Bundle bundle = new Bundle();
                bundle.putLong(PluginConstant.KEY_INFO_USER_ID, userId);
                bundle.putLong(PluginConstant.KEY_INFO_FAMILY_ID, familyId);
                bundle.putString(PluginConstant.KEY_INFO_TICKET, ticket);
                bundle.putInt(PluginConstant.KEY_INFO_TYPE, type);

                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_INFO)
                        .build();
                try {
                    pluginExecutor.send(PluginEngine.CODE_START_PLUGIN, data, bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //智能家居
    public static void startSmartHome() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_SMARTHOME)
                        .build();
                try {
                    pluginExecutor.send(PluginEngine.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //设置
    public static void startSetting() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_SETTING)
                        .build();
                try {
                    pluginExecutor.send(PluginEngine.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //医疗检测
    public static void startMedical() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_MEDICAL)
                        .build();
                try {
                    pluginExecutor.send(PluginEngine.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //对讲
    public static void startTalk() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_TALK)
                        .build();
                try {
                    pluginExecutor.send(PluginEngine.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //更新插件
    public static void update() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {

            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                try {
                    pluginExecutor.send(PluginEngine.CODE_UPDATE_PLUGIN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    public static void initUserInfo(final String fileName) {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                try {
                    String data = HostDataBuilder.saveInfoBuilder()
                            .name(fileName)
                            .build();
                    pluginExecutor.send(PluginEngine.CODE_GET_SAVE_INFO, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    public static void getMedical() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                try {
                    String data = HostDataBuilder.saveInfoBuilder()
                            .name(FILE_MEDICAL)
                            .build();
                    pluginExecutor.send(PluginEngine.CODE_GET_SAVE_INFO, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }
}
