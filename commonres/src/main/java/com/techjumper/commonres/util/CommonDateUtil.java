package com.techjumper.commonres.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by kevin on 16/5/23.
 */
public class CommonDateUtil {

    public static String getTitleDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String week;
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            default:
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;
        }

        return month + "月" + day + "日" + "   周" + week + "  " + hour + ":" + minute;
    }
}
