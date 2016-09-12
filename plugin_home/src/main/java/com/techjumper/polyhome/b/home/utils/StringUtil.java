package com.techjumper.polyhome.b.home.utils;

import android.text.TextUtils;
import android.util.Log;

import com.techjumper.corelib.utils.window.ToastUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 判断长度
     *
     * @param string
     * @param indexId
     * @param position
     * @return
     */
    private static String subString(String string, int indexId, int position) {
        if (TextUtils.isEmpty(string))
            return "";

        String result = string.substring(position, indexId);

        return result.length() > 2 ? result.substring(0, 2) : result;
    }

    public static String delHTMLTag(String htmlStr) {
        if (TextUtils.isEmpty(htmlStr))
            return "";

        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }
}
