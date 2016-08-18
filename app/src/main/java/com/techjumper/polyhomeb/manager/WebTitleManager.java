package com.techjumper.polyhomeb.manager;

import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.utils.WebTitleHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WebTitleManager {

    //标准格式如下
    //http://pl.techjumper.com/neighbor/articles/show/49?title=帖子详情&left=::NativeReturn,::&right=呵呵::,呵呵::
    //其中,参数的格式如下
    //title=帖子详情&left=::NativeReturn,::&right=呵呵::,呵呵::
    //当title没有某个View的时候,比如没有右边第二个,右边第一个,左边第二个,实际格式如下
    //title=帖子详情&left=::NativeReturn&right=

    private static final int LEFT_FIRST = 0;
    private static final int LEFT_SECOND = 1;
    private static final int RIGHT_FIRST = 2;
    private static final int RIGHT_SECOND = 3;

    private String mUrl;
    private String mRealUrl;
    private String mTitle;
    private View mViewRoot;
    private IWebViewTitleClick mIWebViewTitleClick;
    private WebTitleHelper.Builder mWebTitleBuilder;

    /**
     * 图片icon地址
     */
    private String mLeftFirstPicUrl, mLeftSecondPicUrl, mRightFirstPicUrl, mRightSecondPicUrl;

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
    private String mLeftFirstText, mLeftSecondText, mRightFirstText, mRightSecondText;

    /**
     * 左1,左2,右1,右2分别对应本地的哪种按钮(WebTitleHelper中的几种静态常量)
     */
    private int mLeftFirstIconType, mLeftSecondIconType, mRightFirstIconType, mRightSecondIconType;

    public WebTitleManager(String url, View mViewRoot, IWebViewTitleClick mIWebViewTitleClick) {
        JLog.e(url);
        this.mUrl = url;
        this.mViewRoot = mViewRoot;
        this.mIWebViewTitleClick = mIWebViewTitleClick;
        process();
        initWebViewTitle();
    }

    private void process() {

        String[] content = mUrl.split("\\?");
        mRealUrl = content[0];   //http://pl.techjumper.com/neighbor/articles/show/49
        String split = content[1];  //title=帖子详情&left=::NativeReturn,::&right=::,::

        String[] splits = split.split("&");
        try {
            mTitle = URLDecoder.decode(splits[0].replace("title=", ""), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        processLeft(splits[1]); //left=呵呵::NativeReturn  或者  left=呵呵::NativeReturn,哈哈::NativeMenu
        processRight(splits[2]); //right=呵呵::,呵呵::  或者直接是right=(此时代表右边没有东西)

    }

    //left=左1:url:NativeReturn,左2:url:NativeMenu
    private void processLeft(String left) {
        String replace = left.replace("left=", "");//先去掉前面的"left="这部分得到->呵呵::NativeReturn或者->呵呵::NativeReturn,哈哈::NativeMenu

        //如果包含逗号,证明有2个或者2个以上的按钮
        if (replace.indexOf(",") > 0) {
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
            mIsLeftFirstShow = true;
            String[] split__ = s22.split(":");
            String s1_ = split__[0];  //呵呵
            String s2_ = split__[1];  //""(空)
            String s3_ = split__[2];  //NativeReturn

            if (!TextUtils.isEmpty(s1_)) {
                mIsLeftFirstIsTextView = true;
                try {
                    mLeftFirstText = URLDecoder.decode(s1_, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(s2_)) {
                mIsLeftFirstIsTextView = false;
                mLeftFirstText = "";
                try {
                    mLeftFirstPicUrl = URLDecoder.decode(s2_, "UTF-8");
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
                String s1 = split[0];  //呵呵
                String s2 = split[1];  //""(空)
                String s3 = split[2];  //NativeReturn

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
//        String[] leftGroup = replace.split(",");
//        String leftFirst = leftGroup[0];  //左1:url:NativeReturn
//        String leftSecond = leftGroup[1];  //,左2:url:NativeMenu
//
//        //要显示左1部分
//        if (!TextUtils.isEmpty(leftFirst)) {
//            String[] split = leftFirst.split(":");
//            if (split.length == 3) {   //如果有内容,length一定是3,如果全都为空,length为0,不可能出现length为其他值的情况
//                String s1 = split[0];  //左边1的按钮的文字内容,@Nullable
//                String s2 = split[1];  //左边1的按钮的在线图片内容,@Nullable
//                String s3 = split[2];  //左边1的按钮的method响应内容,只要有左1,就不会为空
//
//                if (!TextUtils.isEmpty(s1) || !TextUtils.isEmpty(s2) || !TextUtils.isEmpty(s3)) {
//                    mIsLeftFirstShow = true;
//                } else {
//                    mIsLeftFirstShow = false;
//                }
//                if (!TextUtils.isEmpty(s1)) {
//                    mIsLeftFirstIsTextView = true;
//                    try {
//                        mLeftFirstText = URLDecoder.decode(s1, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!TextUtils.isEmpty(s2)) {
//                    mIsLeftFirstIsTextView = false;
//                    mLeftFirstText = "";
//                    try {
//                        mLeftFirstPicUrl = URLDecoder.decode(s2, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
//                if (!TextUtils.isEmpty(s3)) {
//                    processMethod(s3, LEFT_FIRST);
//                }
//            }
//        }
//
//        //要显示左2
//        if (!TextUtils.isEmpty(leftSecond)) {
//            String replace1 = leftSecond.replace(",", "");//去掉第二部分前面的逗号,得到->左2:url:NativeMenu
//            String[] split = replace1.split(":");
//            if (split.length == 3) {
//                String s1 = split[0];  //左边1的按钮的文字内容,@Nullable
//                String s2 = split[1];  //左边1的按钮的在线图片内容,@Nullable
//                String s3 = split[2];  //左边1的按钮的method响应内容,只要有左1,就不会为空
//
//                if (!TextUtils.isEmpty(s1) || !TextUtils.isEmpty(s2) || !TextUtils.isEmpty(s3)) {
//                    mIsLeftSecondShow = true;
//                } else {
//                    mIsLeftSecondShow = false;
//                }
//                if (!TextUtils.isEmpty(s1)) {
//                    mIsLeftSecondIsTextView = true;
//                    try {
//                        mLeftSecondText = URLDecoder.decode(s1, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!TextUtils.isEmpty(s2)) {
//                    mIsLeftSecondIsTextView = false;
//                    mLeftSecondText = "";
//                    try {
//                        mLeftSecondPicUrl = URLDecoder.decode(s2, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
//                if (!TextUtils.isEmpty(s3)) {
//                    processMethod(s3, LEFT_SECOND);
//                }
//            }
//        }
    }

    private void processRight(String right) {
        String replace = right.replace("right=", "");//先去掉前面的"right="这部分

        //如果包含逗号,证明有2个或者2个以上的按钮
        if (replace.indexOf(",") > 0) {
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
                String s3 = split[2];  //NativeReturn

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
//        String[] rightGroup = replace.split(",");
//        String rightFirst = rightGroup[0];  //右1:url:NativeReturn
//        String rightSecond = rightGroup[1];  //,右2:url:NativeMenu
//
//        //要显示右1部分
//        if (!TextUtils.isEmpty(rightFirst)) {
//            String[] split = rightFirst.split(":");
//            if (split.length == 3) {
//                String s1 = split[0];  //右边1的按钮的文字内容,@Nullable
//                String s2 = split[1];  //右边1的按钮的在线图片内容,@Nullable
//                String s3 = split[2];  //右边1的按钮的method响应内容,只要有右1,就不会为空
//
//                if (!TextUtils.isEmpty(s1) || !TextUtils.isEmpty(s2) || !TextUtils.isEmpty(s3)) {
//                    mIsRightFirstShow = true;
//                } else {
//                    mIsRightFirstShow = false;
//                }
//
//                if (!TextUtils.isEmpty(s1)) {
//                    mIsRightFirstIsTextView = true;
//                    try {
//                        mRightFirstText = URLDecoder.decode(s1, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!TextUtils.isEmpty(s2)) {
//                    mIsRightFirstIsTextView = false;
//                    mRightFirstText = "";
//                    try {
//                        mRightFirstPicUrl = URLDecoder.decode(s2, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
//                if (!TextUtils.isEmpty(s3)) {
//                    processMethod(s3, RIGHT_FIRST);
//                }
//            }
//        }
//
//        //要显示右2
//        if (!TextUtils.isEmpty(rightSecond)) {
//            String replace1 = rightSecond.replace(",", "");//去掉第二部分前面的逗号,得到->右2:url:NativeMenu
//            String[] split = replace1.split(":");
//            if (split.length == 3) {
//                String s1 = split[0];  //右边1的按钮的文字内容,@Nullable
//                String s2 = split[1];  //右边1的按钮的在线图片内容,@Nullable
//                String s3 = split[2];  //右边1的按钮的method响应内容,只要有右1,就不会为空
//
//                if (!TextUtils.isEmpty(s1) || !TextUtils.isEmpty(s2) || !TextUtils.isEmpty(s3)) {
//                    mIsRightSecondShow = true;
//                } else {
//                    mIsRightSecondShow = false;
//                }
//
//                if (!TextUtils.isEmpty(s1)) {
//                    mIsRightSecondIsTextView = true;
//                    try {
//                        mRightSecondText = URLDecoder.decode(s1, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!TextUtils.isEmpty(s2)) {
//                    mIsRightSecondIsTextView = false;
//                    mRightSecondText = "";
//                    try {
//                        mRightSecondPicUrl = URLDecoder.decode(s2, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //虽然不可能为null(只要是要显示的话就不可能为null),但是还是判断一下
//                if (!TextUtils.isEmpty(s3)) {
//                    processMethod(s3, RIGHT_SECOND);
//                }
//            }
//        }
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
                .setLeftFirstIconType(mLeftFirstIconType)
                .setLeftSecondIconType(mLeftSecondIconType)
                .setRightFirstIconType(mRightFirstIconType)
                .setRightSecondIconType(mRightSecondIconType)
                .setLeftFirstText(mLeftFirstText)
                .setLeftSecondText(mLeftSecondText)
                .setRightFirstText(mRightFirstText)
                .setRightSecondText(mRightSecondText)
                .leftFirstClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleLeftFirstClick(mLeftFirstIconType);
                    }
                })
                .leftSecondClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleLeftSecondClick(mLeftSecondIconType);
                    }
                })
                .rightFirstClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleRightFirstClick(mRightFirstIconType);
                    }
                })
                .rightSecondClick(v -> {
                    if (mIWebViewTitleClick != null) {
                        mIWebViewTitleClick.onTitleRightSecondClick(mRightSecondIconType);
                    }
                });
        mWebTitleBuilder.process();
    }

    private void processMethod(String s, int location) {
        switch (location) {
            case LEFT_FIRST:
                switch (s) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mLeftFirstIconType = WebTitleHelper.NATIVE_ICON_TYPE_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mLeftFirstIconType = WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mLeftFirstIconType = WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE;
                        break;
                }
                break;
            case LEFT_SECOND:
                switch (s) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mLeftSecondIconType = WebTitleHelper.NATIVE_ICON_TYPE_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mLeftSecondIconType = WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mLeftSecondIconType = WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE;
                        break;
                }
                break;
            case RIGHT_FIRST:
                switch (s) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mRightFirstIconType = WebTitleHelper.NATIVE_ICON_TYPE_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mRightFirstIconType = WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mRightFirstIconType = WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE;
                        break;
                }
                break;
            case RIGHT_SECOND:
                switch (s) {
                    case WebTitleHelper.NATIVE_METHOD_RETURN:
                        mRightSecondIconType = WebTitleHelper.NATIVE_ICON_TYPE_RETURN;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_MENU:
                        mRightSecondIconType = WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU;
                        break;
                    case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                        mRightSecondIconType = WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE;
                        break;
                }
                break;
        }
    }

    public String getRealUrl() {
        return mRealUrl;
    }

}
