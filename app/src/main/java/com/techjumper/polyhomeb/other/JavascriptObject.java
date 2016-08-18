package com.techjumper.polyhomeb.other;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyCommentActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JavascriptObject {

    private Activity mActivity;

    public JavascriptObject(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //两种情况,event对应事件,没有链接,http对应链接
    //event://NativeNewComment?id=11&token=123232
    //http://www.baidu.com?id=11&type=1
    @JavascriptInterface
    public void pageJump(String url) {
        if (url.startsWith("http")) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.JS_PAGE_JUMP_URL, url);
            new AcHelper.Builder(mActivity).extra(bundle).target(ReplyDetailActivity.class).start();
        } else if (url.startsWith("event")) {
            JLog.e(url);
            //跳转回复帖子界面
            if (url.indexOf("NativeNewComment") > 0) {
                String article_id;
                String comment_id;
                String[] split = url.split("\\?");
                String params = split[1];  //id=11&token=123232
                String[] split1 = params.split("&");
                String s1 = split1[0];//id=11
                String s2 = split1[1];//type=dd
                String[] strings1 = s1.split("=");
                String[] strings2 = s2.split("=");
                //如果strings1的[0]字段和article_id相等,那么这个字段就是article_id字段
                if (Constant.JS_REPLY_ARTICLE_ID.equals(strings1[0])) {
                    if (strings1.length > 1) {
                        article_id = strings1[1];
                    } else {
                        article_id = "";
                    }
                    if (strings2.length > 1) {
                        comment_id = strings2[1];
                    } else {
                        comment_id = "";
                    }
                } else { //这个字段是comment_id
                    if (strings1.length > 1) {
                        article_id = strings2[1];
                    } else {
                        article_id = "";
                    }
                    if (strings2.length > 1) {
                        comment_id = strings1[1];
                    } else {
                        comment_id = "";
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.JS_REPLY_ARTICLE_ID, article_id);
                bundle.putString(Constant.JS_REPLY_COMMENT_ID, comment_id);
                new AcHelper.Builder(mActivity).extra(bundle).target(ReplyCommentActivity.class).start();
            }
        }
    }
}