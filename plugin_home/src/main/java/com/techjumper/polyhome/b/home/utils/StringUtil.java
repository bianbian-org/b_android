package com.techjumper.polyhome.b.home.utils;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/4/28.
 */
public class StringUtil {

    // 拼接两个字符串，并在中间加入反斜杠 类似"23/15"格式
    public static String addSeparator(String firstString, String secondString) {
        String merge = "";
        if (!TextUtils.isEmpty(firstString) && !TextUtils.isEmpty(secondString)) {
            merge = firstString.concat(File.separator).concat(secondString);
        }
        return merge;
    }

    // 根据某个标识截取一段字符串返回我们需要的个数（功能特殊，目前只写死3个，发现新的需求了再抽离出来）
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
