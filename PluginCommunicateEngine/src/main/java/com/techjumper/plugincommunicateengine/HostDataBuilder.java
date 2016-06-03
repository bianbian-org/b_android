package com.techjumper.plugincommunicateengine;

import com.techjumper.plugincommunicateengine.entity.core.StartPluginActivityEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/5/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HostDataBuilder {
//    private static ServiceDataBuilder INSTANCE;

    private HostDataBuilder() {
    }

    //
//    public static ServiceDataBuilder getInstance() {
//        if (INSTANCE == null) {
//            synchronized (ServiceDataBuilder.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new ServiceDataBuilder();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
    public static StartPluginBuilder startPluginBuilder() {
        return new StartPluginBuilder();
    }

    public static final class StartPluginBuilder {
        private String packageName;
        private String activityName;

        private StartPluginBuilder() {
        }

        public StartPluginBuilder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public StartPluginBuilder activityName(String activityName) {
            this.activityName = activityName;
            return this;
        }

        public String build() {
            StartPluginActivityEntity startPluginActivityEntity = new StartPluginActivityEntity();
            StartPluginActivityEntity.DataEntity dataEntity = new StartPluginActivityEntity.DataEntity();
            dataEntity.setPackageName(packageName);
            dataEntity.setActivityName(activityName);
            startPluginActivityEntity.setData(dataEntity);
            return GsonUtils.toJson(startPluginActivityEntity);
        }
    }
}
