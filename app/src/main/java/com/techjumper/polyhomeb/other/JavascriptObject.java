package com.techjumper.polyhomeb.other;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.JS2JavaBaseEntity;
import com.techjumper.polyhomeb.entity.JS2JavaImageViewEntity;
import com.techjumper.polyhomeb.entity.JS2JavaNotificationEntity;
import com.techjumper.polyhomeb.entity.JS2JavaPageJumpEntity;
import com.techjumper.polyhomeb.entity.event.WebViewNotificationEvent;
import com.techjumper.polyhomeb.mvp.p.activity.LoginActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyCommentActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.WebViewShowBigPicActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;

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
     * 跳转界面:回复评论,帖子详情
     */
    //两种情况,event对应事件,没有链接,http对应链接
    //event://NativeNewComment?id=11&token=123232
    //http://www.baidu.com?id=11&type=1
    //pay://order_num=11111&total=11.0
    private void pageJump(String url) {
        if (url.startsWith("http")) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.JS_PAGE_JUMP_URL, url);
            new AcHelper.Builder(mActivity).extra(bundle).target(ReplyDetailActivity.class).start();
        } else if (url.startsWith("event")) {
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

    /**
     * 查看大图
     */
    private void imageView(int index, String[] urls) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.JS_BIG_PIC_INDEX, index);
        bundle.putStringArray(Constant.JS_BIG_PIC_ARRAY, urls);
        new AcHelper.Builder(mActivity).extra(bundle).target(WebViewShowBigPicActivity.class).start();
    }

    /**
     * 刷新
     */
    private void response() {
//        RxBus.INSTANCE.send(new RefreshStopEvent());
    }

    /**
     * 总的交互入口,根据json来判断执行的操作
     */
    @JavascriptInterface
    public void postMessage(String json) {

        if (TextUtils.isEmpty(json)) return;
        JS2JavaBaseEntity baseEntity = GsonUtils.fromJson(json, JS2JavaBaseEntity.class);
        if (baseEntity == null) return;
        String method = baseEntity.getMethod();
        if (TextUtils.isEmpty(method)) return;

        switch (method) {
            case "PageJump":
                JS2JavaPageJumpEntity js2JavaPageJumpEntity = GsonUtils.fromJson(json, JS2JavaPageJumpEntity.class);
                JS2JavaPageJumpEntity.ParamsBean params = js2JavaPageJumpEntity.getParams();
                String url = params.getUrl();
                pageJump(url);
                break;
            case "ImageView":
                JS2JavaImageViewEntity js2JavaImageViewEntity = GsonUtils.fromJson(json, JS2JavaImageViewEntity.class);
                JS2JavaImageViewEntity.ParamsBean params1 = js2JavaImageViewEntity.getParams();
                int index = params1.getIndex();
                List<String> images = params1.getImages();
                String[] imageArray = new String[images.size()];
                for (int i = 0; i < images.size(); i++) {
                    imageArray[i] = images.get(i);
                }
                imageView(index, imageArray);
                break;
            case "RefreshLoaded":
                response();
                break;
            case "RefreshNotice":
                //2016/10/26  友邻网页做判断，如果是家庭权限就能点，如果不是就不能点(客户端做或者H5做都行.最好是客户端做)
                if (UserManager.INSTANCE.isFamily()) {
                    JS2JavaNotificationEntity js2JavaNotificationEntity = GsonUtils.fromJson(json, JS2JavaNotificationEntity.class);
                    JS2JavaNotificationEntity.ParamsBean paramsBean = js2JavaNotificationEntity.getParams();
                    String result = paramsBean.getResult();
                    RxBus.INSTANCE.send(new WebViewNotificationEvent(result));
                }
                break;
            case "login":
                Bundle bundle = new Bundle();
                bundle.putString(LoginActivityPresenter.KEY_COME_FROM, LoginActivityPresenter.VALUE_COME_FROM_WEBVIEW);
                new AcHelper.Builder(mActivity).extra(bundle).closeCurrent(false).target(LoginActivity.class).start();
                break;
            case "pay":
                //解析json字符串
                //根据字符串的内容，选择支付方式
                //开始支付
//                PayManager.with().loadPay(null, mActivity, 1, null);
                break;

        }

    }

}