package com.techjumper.polyhomeb.manager;

import android.text.TextUtils;
import android.view.View;

import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.utils.WebTitleHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.techjumper.polyhomeb.utils.WebTitleHelper.NATIVE_METHOD_DELETE_ARTICLE;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WebTitleManager {

    //标准格式如下

    //http://pl.techjumper.com/neighbor/articles/show/49?title=帖子详情&left=::NativeReturn,::&right=呵呵::,呵呵::&pageName=购物车
    //其中,参数的格式如下
    //title=帖子详情&left=::NativeReturn,::&right=呵呵::,呵呵::
    //当title没有某个View的时候,比如没有右边第二个,右边第一个,左边第二个,实际格式如下
    //title=帖子详情&left=::NativeReturn&right=

    //pageName=mypl,作用是判断商城当前在不在"我的"界面，如果在，就有这个字段，并且值为mypl，反之则没有这个字段和值.
    //pageReturn,作用是判断商城当前在不在"结算"界面，如果在就有这个字段，并且值为home，反之则没有这个字段和值.

    private static final int LEFT_FIRST = 1;
    private static final int LEFT_SECOND = 2;
    private static final int RIGHT_FIRST = 3;
    private static final int RIGHT_SECOND = 4;
    private String mUrl;
    private String mRealUrl;
    private String mTitle;
    private View mViewRoot;
    private IWebViewTitleClick mIWebViewTitleClick;
    private WebTitleHelper.Builder mWebTitleBuilder;
    private String mPageName = "";
    private String mPageReturn = "";

    /**
     * 图片icon地址
     */
    private String mLeftFirstPicUrl = "", mLeftSecondPicUrl = "", mRightFirstPicUrl = "", mRightSecondPicUrl = "";

    /**
     * 是否显示左1,左2,右1,右2
     */
    private boolean mIsLeftFirstShow, mIsLeftSecondShow, mIsRightFirstShow, mIsRightSecondShow;

    /**
     * 左1,左2,右1,右2是不是显示文字,也即是"是不是TextView"
     */
    private boolean mIsLeftFirstIsTextView, mIsLeftSecondIsTextView, mIsRightFirstIsTextView, mIsRightSecondIsTextView;

    /**
     * 左1,左2,右1,右2分别显示的是什么文字?
     */
    private String mLeftFirstText = "", mLeftSecondText = "", mRightFirstText = "", mRightSecondText = "";

    /**
     * 左1,左2,右1,右2分别对应本地的哪种按钮(WebTitleHelper中的几种静态常量)
     */
    private String mLeftFirstMethod = "", mLeftSecondMethod = "", mRightFirstMethod = "", mRightSecondMethod = "";

    public WebTitleManager(String url, View mViewRoot, IWebViewTitleClick mIWebViewTitleClick) {
//        JLog.e(url);
        this.mUrl = url;
        this.mViewRoot = mViewRoot;
        this.mIWebViewTitleClick = mIWebViewTitleClick;
        process();
        initWebViewTitle();
    }

    private void process() {

        String[] content = mUrl.split("\\?");
        mRealUrl = content[0];   //http://pl.techjumper.com/neighbor/articles/show/49
        String split = content[1];  //title=帖子详情&left=::NativeReturn,::&right=::,::&refresh=refresh

        String[] splits = split.split("&");

        //处理title
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].contains("title=")) {
                try {
                    mTitle = URLDecoder.decode(splits[i].replace("title=", ""), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        //处理左边
        for (int i = 0; i < splits.length; i++) {  //因为0是title
            if (splits[i].contains("left=")) {
                processLeft(splits[i]); //left=呵呵::NativeReturn  或者  left=呵呵::NativeReturn,哈哈::NativeMenu
                break;
            }
        }

        //处理右边
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].contains("right=")) {
                processRight(splits[i]); //right=呵呵::,呵呵::  或者直接是right=(此时代表右边没有东西)
                break;
            }
        }

        //处理pageName
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].contains("pageName=")) {
                String tag = splits[i];
                mPageName = tag.replace("pageName=", "");
                break;
            }
        }

        //处理pageReturn
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].contains("pageReturn=")) {
                String tag = splits[i];
                mPageReturn = tag.replace("pageReturn=", "");
                break;
            }
        }
    }

    //left=左1:url:NativeReturn,左2:url:NativeMenu
    private void processLeft(String left) {

        String replace = left.replace("left=", "");//先去掉前面的"left="这部分得到->呵呵::NativeReturn或者->呵呵::NativeReturn,哈哈::NativeMenu

        //如果包含逗号,证明有2个或者2个以上的按钮
//        if (replace.indexOf(",") > 0) {
        if (replace.contains(",")) {
            String[] split = replace.split(",");//得到->呵呵::NativeReturn 以及得到->哈哈::NativeMenu
            String s11 = split[0]; //左边第一个
            String s22 = split[1];  //左边第二个

            /*---------左1-----------*/
            mIsLeftFirstShow = true;
            String[] split_ = s11.split(":");
            String s1 = split_[0];  //呵呵
            String s2 = split_[1];  //""(空)
            String s3 = split_[2];  //NativeReturn

            if (!TextUtils.isEmpty(s1)) {
                mIsLeftFirstIsTextView = true;
                try {
                    mLeftFirstText = URLDecoder.decode(s1, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(s2)) {
                mIsLeftFirstIsTextView = false;
                mLeftFirstText = "";
                try {
                    mLeftFirstPicUrl = URLDecoder.decode(s2, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
            if (!TextUtils.isEmpty(s3)) {
                processMethod(s3, LEFT_FIRST);
            }

            /*----------左2----------*/
            mIsLeftSecondShow = true;
            String[] split__ = s22.split(":");
            String s1_ = split__[0];  //呵呵
            String s2_ = split__[1];  //""(空)
            String s3_ = split__[2];  //NativeReturn

            if (!TextUtils.isEmpty(s1_)) {
                mIsLeftSecondIsTextView = true;
                try {
                    mLeftSecondText = URLDecoder.decode(s1_, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(s2_)) {
                mIsLeftSecondIsTextView = false;
                mLeftSecondText = "";
                try {
                    mLeftSecondPicUrl = URLDecoder.decode(s2_, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
            if (!TextUtils.isEmpty(s3_)) {
                processMethod(s3_, LEFT_SECOND);
            }

        } else { //没有逗号,就只有一个按钮或者没有按钮 (一个按钮->呵呵::NativeReturn)(没有按钮->""(直接是空的))
            if (TextUtils.isEmpty(replace)) { //没有按钮
                mIsLeftFirstShow = false;
                mLeftFirstText = "";
                mLeftSecondText = "";
            } else {  //一个按钮
                mIsLeftFirstShow = true;
                String[] split = replace.split(":");
                String s1 = split[0];  //呵呵(可能为null)
                String s2 = split[1];  //""(可能为null)
                String s3 = split[2];  //NativeReturn(一定不为null)

                if (!TextUtils.isEmpty(s1)) {
                    mIsLeftFirstIsTextView = true;
                    try {
                        mLeftFirstText = URLDecoder.decode(s1, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(s2)) {
                    mIsLeftFirstIsTextView = false;
                    mLeftFirstText = "";
                    try {
                        mLeftFirstPicUrl = URLDecoder.decode(s2, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
                if (!TextUtils.isEmpty(s3)) {
                    processMethod(s3, LEFT_FIRST);
                }
            }
        }
    }

    private void processRight(String right) {
        String replace = right.replace("right=", "");//先去掉前面的"right="这部分

        //如果包含逗号,证明有2个或者2个以上的按钮
//        if (replace.indexOf(",") > 0) {
        if (replace.contains(",")) {
            String[] split = replace.split(",");//得到->呵呵::NativeReturn 以及得到->哈哈::NativeMenu
            String s11 = split[0]; //右边第一个
            String s22 = split[1];  //右边第二个

            /*---------右1-----------*/
            mIsRightFirstShow = true;
            String[] split_ = s11.split(":");
            String s1 = split_[0];  //呵呵
            String s2 = split_[1];  //""(空)
            String s3 = split_[2];  //NativeReturn

            if (!TextUtils.isEmpty(s1)) {
                mIsRightFirstIsTextView = true;
                try {
                    mRightFirstText = URLDecoder.decode(s1, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(s2)) {
                mIsRightFirstIsTextView = false;
                mRightFirstText = "";
                try {
                    mRightFirstPicUrl = URLDecoder.decode(s2, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
            if (!TextUtils.isEmpty(s3)) {
                processMethod(s3, RIGHT_FIRST);
            }

            /*----------右2----------*/
            mIsRightSecondShow = true;
            String[] split__ = s22.split(":");
            String s1_ = split__[0];  //呵呵
            String s2_ = split__[1];  //""(空)
            String s3_ = split__[2];  //NativeReturn

            if (!TextUtils.isEmpty(s1_)) {
                mIsRightSecondIsTextView = true;
                try {
                    mRightSecondText = URLDecoder.decode(s1_, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(s2_)) {
                mIsRightSecondIsTextView = false;
                mRightSecondText = "";
                try {
                    mRightSecondPicUrl = URLDecoder.decode(s2_, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
            if (!TextUtils.isEmpty(s3_)) {
                processMethod(s3_, RIGHT_SECOND);
            }

        } else { //没有逗号,就只有一个按钮或者没有按钮 (一个按钮->呵呵::NativeReturn)(没有按钮->""(直接是空的))
            if (TextUtils.isEmpty(replace)) { //没有按钮
                mIsRightFirstShow = false;
                mRightFirstText = "";
                mRightSecondText = "";
            } else {  //一个按钮

                mIsRightFirstShow = true;
                String[] split = replace.split(":");
                String s1 = split[0];  //呵呵
                String s2 = split[1];  //""(空)
                String s3 = split[2];  //NativeReturn或者是一个不固定的method,如果不是Native这种,那我需要将这个method保存下来,给Activity,然后js调用它

                if (!TextUtils.isEmpty(s1)) {
                    mIsRightFirstIsTextView = true;
                    try {
                        mRightFirstText = URLDecoder.decode(s1, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(s2)) {
                    mIsRightFirstIsTextView = false;
                    mRightFirstText = "";
                    try {
                        mRightFirstPicUrl = URLDecoder.decode(s2, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
                if (!TextUtils.isEmpty(s3)) {
                    processMethod(s3, RIGHT_FIRST);
                }
            }
        }
    }

    private String getTitle() {
        if (TextUtils.isEmpty(mTitle)) return "";
        return mTitle;
    }

    private void initWebViewTitle() {
        mWebTitleBuilder = WebTitleHelper.create(mViewRoot)
                .title(getTitle())
                .showLeftFirst(mIsLeftFirstShow)
                .showLeftSecond(mIsLeftSecondShow)
                .showRightFirst(mIsRightFirstShow)
                .showRightSecond(mIsRightSecondShow)
                .leftFirstIsTextView(mIsLeftFirstIsTextView)
                .leftSecondIsTextView(mIsLeftSecondIsTextView)
                .rightFirstIsTextView(mIsRightFirstIsTextView)
                .rightSecondIsTextView(mIsRightSecondIsTextView)
                .setLeftFirstPicUrl(mLeftFirstPicUrl)
                .setLeftSecondPicUrl(mLeftSecondPicUrl)
                .setRightFirstPicUrl(mRightFirstPicUrl)
                .setRightSecondPicUrl(mRightSecondPicUrl)
                .setLeftFirstIconType(mLeftFirstMethod)
                .setLeftSecondIconType(mLeftSecondMethod)
                .setRightFirstIconType(mRightFirstMethod)
                .setRightSecondIconType(mRightSecondMethod)
                .setLeftFirstText(mLeftFirstText)
                .setLeftSecondText(mLeftSecondText)
                .setRightFirstText(mRightFirstText)
                .setRightSecondText(mRightSecondText)
                .leftFirstClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleLeftFirstClick(mLeftFirstMethod);
                    }
                })
                .leftSecondClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleLeftSecondClick(mLeftSecondMethod);
                    }
                })
                .rightFirstClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleRightFirstClick(mRightFirstMethod);
                    }
                })
                .rightSecondClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleRightSecondClick(mRightSecondMethod);
                    }
                });
        mWebTitleBuilder.process();
    }

    private void processMethod(String method, int location) {
        switch (location) {
            case LEFT_FIRST:
                switch (method) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mLeftFirstMethod = WebTitleHelper.NATIVE_METHOD_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mLeftFirstMethod = WebTitleHelper.NATIVE_METHOD_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mLeftFirstMethod = WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE;
                        break;
                    default:
                        mLeftFirstMethod = method;
                        break;
                }
                break;
            case LEFT_SECOND:
                switch (method) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mLeftSecondMethod = WebTitleHelper.NATIVE_METHOD_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mLeftSecondMethod = WebTitleHelper.NATIVE_METHOD_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mLeftSecondMethod = WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE;
                        break;
                    default:
                        mLeftSecondMethod = method;
                        break;

                }
                break;
            case RIGHT_FIRST:
                switch (method) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mRightFirstMethod = WebTitleHelper.NATIVE_METHOD_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mRightFirstMethod = WebTitleHelper.NATIVE_METHOD_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mRightFirstMethod = WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE;
                        break;
                    case NATIVE_METHOD_DELETE_ARTICLE:
                        mRightFirstMethod = WebTitleHelper.NATIVE_METHOD_DELETE_ARTICLE;
                        break;
                    default:
                        mRightFirstMethod = method;
                        break;
                }
                break;
            case RIGHT_SECOND:
                switch (method) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mRightSecondMethod = WebTitleHelper.NATIVE_METHOD_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mRightSecondMethod = WebTitleHelper.NATIVE_METHOD_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mRightSecondMethod = WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE;
                        break;
                    case NATIVE_METHOD_DELETE_ARTICLE:
                        mRightSecondMethod = WebTitleHelper.NATIVE_METHOD_DELETE_ARTICLE;
                        break;
                    default:
                        mRightSecondMethod = method;
                        break;
                }
                break;
        }
    }

    public String getRealUrl() {
        return mRealUrl;
    }

    public String getPageName() {
        return mPageName;
    }

    public String getPageReturn() {
        return mPageReturn;
    }

}
