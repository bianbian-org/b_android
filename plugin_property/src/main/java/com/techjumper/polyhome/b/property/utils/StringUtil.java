package com.techjumper.polyhome.b.property.utils;

import android.text.TextUtils;

/**
 * Created by kevin on 16/5/20.
 */
public class StringUtil {

    public static String formatMessageDate(String date) {
        if (TextUtils.isEmpty(date))
            return "";

        int position = date.indexOf(" ");
        String messageDate = date.substring(position, date.length());
        return TextUtils.isEmpty(messageDate) ? "" : messageDate;
    }
}
