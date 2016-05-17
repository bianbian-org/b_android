package com.techjumper.polyhome.b.property.utils;

/**
 * Created by kevin on 16/5/17.
 */
public class TypeUtil {

    public static String getComplanitTypeString(int type) {
        String typeString = "";
        if (type == 1) {
            typeString = "投诉";
        } else if (type == 2) {
            typeString = "建议";
        } else {
            typeString = "表扬";
        }
        return typeString;
    }
}
