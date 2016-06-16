package com.techjumper.polyhome.b.home.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Created by kevin on 16/6/16.
 */
public class DateUtil {

    public static final String getDate(String date) {
        if (TextUtils.isEmpty(date))
            return "";

        if (date.equals("周一")) {
            date = "周日";
        } else if (date.equals("周二")) {
            date = "周一";
        } else if (date.equals("周三")) {
            date = "周二";
        } else if (date.equals("周四")) {
            date = "周三";
        } else if (date.equals("周五")) {
            date = "周四";
        } else if (date.equals("周六")) {
            date = "周五";
        } else if (date.equals("周日")) {
            date = "周六";
        }
        return date;
    }

    //将默认的2016-1-1转化为1月1日
    public static final SpannableString formatDate(String date) {
        if (TextUtils.isEmpty(date))
            return new SpannableString("");

        int firstPosition = date.indexOf("-");
        int lastPostion = date.lastIndexOf("-");

        String firstDate = date.substring(firstPosition + 1, lastPostion);
        String lastDate = date.substring(lastPostion + 1, date.length());

        String dateString = firstDate + "月" + lastDate + "日";

        int monthPosition = dateString.indexOf("月");
        int dayPosition = dateString.indexOf("日");

        SpannableString spannableString = new SpannableString(dateString);

        spannableString.setSpan(new AbsoluteSizeSpan(14, true), monthPosition, monthPosition + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(14, true), dayPosition, dateString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
