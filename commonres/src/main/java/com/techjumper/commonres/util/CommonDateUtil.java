package com.techjumper.commonres.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kevin on 16/5/23.
 */
public class CommonDateUtil {

    private static Calendar calendar;
    private static long hour;
    private static String weekstr;

    public static String getTitleDate() {
        initCalendar();
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String week;

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

        return month + "月" + day + "日" + "   周" + week + "  " + formatHourMinute(hour) + ":" + formatHourMinute(minute);
    }

    public static String getTitleNewDate(long time) {
        Date date = new Date(time * 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = simpleDateFormat.format(date);

        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String timeString = simpleDateFormat.format(date);

        String year = dateString.substring(0, 4);
        String month = dateString.substring(4, 6);
        String day = dateString.substring(6, 8);

        String weekDay = getWeekDay(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));

        return formatHourMinute(month) + "月" + formatHourMinute(day) + "日" + "   " + weekDay + "  " + timeString;
    }

    private static String getWeekDay(int y, int m, int d) {
        if (m == 1) {
            m = 13;
            y--;
        }
        if (m == 2) {
            m = 14;
            y--;
        }

        int week = (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7;
        switch (week) {
            case 0:
                weekstr = "周一";
                break;
            case 1:
                weekstr = "周二";
                break;
            case 2:
                weekstr = "周三";
                break;
            case 3:
                weekstr = "周四";
                break;
            case 4:
                weekstr = "周五";
                break;
            case 5:
                weekstr = "周六";
                break;
            case 6:
                weekstr = "周日";
                break;
        }
        return weekstr;
    }

    private static String formatHourMinute(String content) {
        if (content.length() == 1)
            return "0".concat(content);

        return content;
    }

    private static void initCalendar() {
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }

    /**
     * 获取09：21格式
     *
     * @return
     */
    public static String getTime() {
        initCalendar();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));

        return formatHourMinute(hour) + ":" + formatHourMinute(minute);
    }

    /**
     * 获取小时
     *
     * @return
     */
    public static int getHour() {
        initCalendar();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     *
     * @return
     */
    public static int getMinute() {
        initCalendar();
        return calendar.get(Calendar.MINUTE);
    }

    public static String getSecond(long time) {
        Date date = new Date(time * 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
        String ssString = simpleDateFormat.format(date);
        return ssString;
    }

    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getCurrentTime(long time) {
        Date date = new Date(time * 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static long delayToPoint() {
        initCalendar();
        int currentSecond = calendar.get(Calendar.SECOND);
        return (60 - currentSecond) * 1000;
    }

    public static long delayToNewPoint() {
        initCalendar();
        int currentSecond = calendar.get(Calendar.SECOND);
        Log.d("submitOnline", "多少秒更新" + (60 - currentSecond));
        return (60 - currentSecond);
    }

    public static long delayToPoint(long time) {
        Date date = new Date(time * 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
        String ssString = simpleDateFormat.format(date);
        Log.d("submitOnline", "多少秒更新" + (60 - Integer.valueOf(ssString)));
        return 60 - Integer.valueOf(ssString);
    }

    /**
     * 一个时间和当前时间比较差多少小时
     *
     * @param time
     * @return
     */
    public static long differHour(String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date timeDate = simpleDateFormat.parse(time);
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间

            long l = curDate.getTime() - timeDate.getTime();
            long day = l / (24 * 60 * 60 * 1000);//天
            hour = (l / (60 * 60 * 1000) - day * 24);//时
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);//分
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);//秒

            Log.d("adclick", "day: " + day + " hour" + hour + " min" + min + " s" + s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return hour;
    }
}
