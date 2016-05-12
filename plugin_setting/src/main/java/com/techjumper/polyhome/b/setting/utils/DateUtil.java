package com.techjumper.polyhome.b.setting.utils;

/**
 * Created by kevin on 16/5/11.
 */
public class DateUtil {

    public static String getDateLink(int year, int month, int day, String link) {
        return new StringBuffer().append(year).append(link)
                .append(month).append(link)
                .append(day).toString();
    }
}
