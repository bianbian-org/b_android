package com.techjumper.polyhome.b.property.utils;


import com.techjumper.polyhome.b.property.Constant;

/**
 * Created by kevin on 16/5/17.
 */
public class TypeUtil {

    public static String getComplanitTypeString(int type) {

        String typeString = "";
        if (type == Constant.LC_COM) {
            typeString = "投诉";
        } else if (type == Constant.LC_SUG) {
            typeString = "建议";
        } else {
            typeString = "表扬";
        }
        return typeString;
    }

    public static String getRepairTitle(int type, int device) {
        String typeString = "";
        String deviceString = "";

        if (type == Constant.LR_TYPE_COM) {
            typeString = "公共报修";
        } else {
            typeString = "个人报修";
        }

        if (device == Constant.LR_DEVICE_LOCK) {
            deviceString = "锁类";
        } else if (device == Constant.LR_DEVICE_WATER) {
            deviceString = "水电类";
        } else if (device == Constant.LR_DEVICE_DOOR) {
            deviceString = "门窗类";
        } else if (device == Constant.LR_DEVICE_WALL) {
            deviceString = "墙类";
        } else {
            deviceString = "电梯类";
        }

        return typeString + "-" + deviceString;
    }
}
