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
}
