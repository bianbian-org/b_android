package com.techjumper.polyhome.utils;

import android.text.TextUtils;

import com.techjumper.corelib.utils.basic.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kevin on 16/5/11.
 */
public class StringUtil extends StringUtils {
    /**
     * 判断是否是email
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email))
            return true;

        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
