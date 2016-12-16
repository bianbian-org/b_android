package com.techjumper.polyhomeb.other;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.JSJavaBaseEntity;
import com.techjumper.polyhomeb.entity.JSJavaContactShopEntity;
import com.techjumper.polyhomeb.entity.JSJavaGetArticleIdEntity;
import com.techjumper.polyhomeb.entity.JSJavaImageViewEntity;
import com.techjumper.polyhomeb.entity.JSJavaNotificationEntity;
import com.techjumper.polyhomeb.entity.JSJavaPageJumpEntity;
import com.techjumper.polyhomeb.entity.PayEntity;
import com.techjumper.polyhomeb.entity.event.H5PayEvent;
import com.techjumper.polyhomeb.entity.event.JSArticleIdEvent;
import com.techjumper.polyhomeb.entity.event.JSCallPhoneNumberEvent;
import com.techjumper.polyhomeb.entity.event.WebViewNotificationEvent;
import com.techjumper.polyhomeb.mvp.p.activity.LoginActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.activity.JSInteractionActivity;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.ReplyCommentActivity;
import com.techjumper.polyhomeb.mvp.v.activity.WebViewShowBigPicActivity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JavascriptObject {

    private Activity mActivity;
    private String url = "";
    private long lastClickTime;

    public JavascriptObject(Activity mActivity) {
        this.mActivity = mActivity;
    }

    private boolean isFastDoubleClick(String url) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        boolean flag = this.url.equalsIgnoreCase(url);
        if (0 < timeD && timeD < 800 && flag) {
            this.url = url;
            return true;
        }
        this.url = url;
        lastClickTime = time;
        return false;
    }

    /**
     * 总的交互入口,根据json来判断执行的操作
     */
    @JavascriptInterface
    public void postMessage(String json) {
        JLog.e(json+"");
        if (TextUtils.isEmpty(json)) return;
        JSJavaBaseEntity baseEntity = GsonUtils.fromJson(json, JSJavaBaseEntity.class);
        if (baseEntity == null) return;
        String method = baseEntity.getMethod();
        if (TextUtils.isEmpty(method)) return;

        switch (method) {
            case "PageJump":
                JSJavaPageJumpEntity jsJavaPageJumpEntity = GsonUtils.fromJson(json, JSJavaPageJumpEntity.class);
                JSJavaPageJumpEntity.ParamsBean params = jsJavaPageJumpEntity.getParams();
                String url = params.getUrl();
                if (!isFastDoubleClick(url)) {
                    pageJump(url);
                }
                break;
            case "ImageView":
                JSJavaImageViewEntity jsJavaImageViewEntity = GsonUtils.fromJson(json, JSJavaImageViewEntity.class);
                JSJavaImageViewEntity.ParamsBean params1 = jsJavaImageViewEntity.getParams();
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
//                if (UserManager.INSTANCE.isFamily()) {
                JSJavaNotificationEntity jsJavaNotificationEntity = GsonUtils.fromJson(json, JSJavaNotificationEntity.class);
                JSJavaNotificationEntity.ParamsBean paramsBean = jsJavaNotificationEntity.getParams();
                String result = paramsBean.getResult();
                RxBus.INSTANCE.send(new WebViewNotificationEvent(result));
//                }
                break;
            case "login":
                Bundle bundle = new Bundle();
                bundle.putString(LoginActivityPresenter.KEY_COME_FROM, LoginActivityPresenter.VALUE_COME_FROM_WEBVIEW);
                new AcHelper.Builder(mActivity).extra(bundle).closeCurrent(false).target(LoginActivity.class).start();
                break;
            case "pay":
                h5Pay(json);
                break;
            case "ContactShop":
                contactShop(json);
                break;
            case "GetArticleId":
                getArticleId(json);
                break;
            case "PageRegister":
                //似乎没什么用
                break;

        }
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
            new AcHelper.Builder(mActivity).extra(bundle).target(JSInteractionActivity.class).start();
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

    private void h5Pay(String json) {
        PayEntity payEntity = GsonUtils.fromJson(json, PayEntity.class);
        if (payEntity == null) return;

        RxBus.INSTANCE.send(new H5PayEvent(mActivity.hashCode(), payEntity));


    }

    //网页上点击拨打电话
    private void contactShop(String json) {
        JSJavaContactShopEntity jsJavaContactShopEntity = GsonUtils.fromJson(json, JSJavaContactShopEntity.class);
        if (jsJavaContactShopEntity == null
                && jsJavaContactShopEntity.getParams() == null) return;
        JSJavaContactShopEntity.ParamsBean paramsBean = jsJavaContactShopEntity.getParams();
        if (TextUtils.isEmpty(paramsBean.getTel()) || TextUtils.isEmpty(paramsBean.getShop_service_id())
                || TextUtils.isEmpty(paramsBean.getStore_id())) return;
        //关于为什么要传入hashCode:
        //比对当前Activity和event传过来的hashCode，必须相同才进入下一步，否则return
        //因为JSInteractionActivity启动模式是standard，开启多少就有多少个实例在Activity栈
        //所以当栈中已经存在数个JSInteractionActivity的时候，JavascriptObject中再发消息，
        //所有的JSInteractionActivity都会收到那个消息，那么就都会走下面的步骤，也就都会弹出对话框
        //造成的问题就是，当前界面弹出对话框，关闭当前界面之后，露出来的JSInteractionActivity会显示一个对话框
        //所以此处比对哈希值，如若发现哈希值不同，证明是JavascriptObject发出消息之后，
        //JSInteractionActivity1和JSInteractionActivity2以及...JSInteractionActivityN都收到消息了
        //但是由于哈希值不同，所以能分辨
        //我在JSInteractionActivity1中点击了拨打电话按钮，那么就只应该在JSInteractionActivity1上创建对话框，
        //其他JSInteractionActivity2，JSInteractionActivity3，JSInteractionActivityN哈希值不同，
        //即使收到了消息，也不能创建对话框，所以哈希值相同的情况下，能证明我在JSInteractionActivity1中的JavascriptObject
        //发消息出来，JSInteractionActivity1收到了，就弹出对话框.
        RxBus.INSTANCE.send(new JSCallPhoneNumberEvent(mActivity.hashCode(), paramsBean.getTel(), paramsBean.getStore_id()
                , paramsBean.getShop_service_id()));
    }

    /**
     * 获取友邻帖子ID
     */
    private void getArticleId(String json) {
        JSJavaGetArticleIdEntity jsJavaGetArticleIdEntity = GsonUtils.fromJson(json, JSJavaGetArticleIdEntity.class);
        JSJavaGetArticleIdEntity.ParamsBean params = jsJavaGetArticleIdEntity.getParams();
        if (params == null) return;
        String article_id = params.getArticle_id();
        if (TextUtils.isEmpty(article_id)) return;
        RxBus.INSTANCE.send(new JSArticleIdEvent(article_id));
    }

}