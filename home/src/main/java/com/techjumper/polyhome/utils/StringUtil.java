package com.techjumper.polyhome.utils;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/4/28.
 */
public class StringUtil {

    // 类似23/15格式
    public static String addSeparator(String firstString, String secondString) {
        String merge = "";
        if (!TextUtils.isEmpty(firstString) && !TextUtils.isEmpty(secondString)) {
            merge = firstString.concat(File.separator).concat(secondString);
        }
        return merge;
    }

    public static List<String> interceptString(String targetString, String indexOfString) {
        List<String> strings = new ArrayList<String>();
        int position = 0;
        if (!TextUtils.isEmpty(targetString)) {
            for (int i = 0; i < 3; i++) {
                int index = targetString.indexOf(indexOfString);
                strings.add(subString(targetString, index, position));
                targetString = targetString.substring(index + 1, targetString.length());
            }
        }

        return strings;
    }

    private static String subString(String string, int indexId, int position) {
        return TextUtils.isEmpty(string) ? "" : string.substring(position, indexId);
    }
}
