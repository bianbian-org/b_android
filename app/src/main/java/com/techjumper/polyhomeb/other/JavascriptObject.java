package com.techjumper.polyhomeb.other;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.techjumper.corelib.utils.common.AcHelper;
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

    /**
     * 回复评论,帖子.
     *
     * @param article_id
     * @param comment_id
     */
    @JavascriptInterface
    public void comment(String article_id, String comment_id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.JS_REPLY_ARTICLE_ID, article_id);
        bundle.putString(Constant.JS_REPLY_COMMENT_ID, comment_id);
        new AcHelper.Builder(mActivity).extra(bundle).target(ReplyCommentActivity.class).start();
    }

    /**
     * 帖子详情.
     *
     * @param url
     */
    @JavascriptInterface
    public void pageJump(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.JS_PAGE_JUMP_URL, url);
        new AcHelper.Builder(mActivity).extra(bundle).target(ReplyDetailActivity.class).start();
    }


}
