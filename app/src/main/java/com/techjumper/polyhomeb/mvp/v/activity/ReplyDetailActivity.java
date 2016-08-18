package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ReloadWebPageEvent;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.manager.WebTitleManager;
import com.techjumper.polyhomeb.mvp.p.activity.ReplyDetailActivityPresenter;
import com.techjumper.polyhomeb.utils.WebTitleHelper;
import com.techjumper.polyhomeb.widget.PolyWebView;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ReplyDetailActivityPresenter.class)
public class ReplyDetailActivity extends AppBaseActivity<ReplyDetailActivityPresenter> implements IWebViewTitleClick {

    @Bind(R.id.wb)
    PolyWebView mWebView;
    @Bind(R.id.left_first_iv)
    ImageView iv;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_reply_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String url = getPresenter().getUrl();
        new WebTitleManager(url, mViewRoot, this);
        mWebView.addJsInterface(this, Constant.JS_REPLY_ARTICLE);
        mWebView.processBack();
        mWebView.loadUrl(url);
    }

    @Override
    protected boolean isWebViewActivity() {
        return true;
    }

    @Override
    public void onTitleLeftFirstClick(int mLeftFirstIconType) {
        switch (mLeftFirstIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                RxBus.INSTANCE.send(new ReloadWebPageEvent());
                finish();
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                ToastUtils.show("左1响应菜单");
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                ToastUtils.show("左1响应新建文章");
                break;
        }
    }

    @Override
    public void onTitleLeftSecondClick(int mLeftSecondIconType) {
        switch (mLeftSecondIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                RxBus.INSTANCE.send(new ReloadWebPageEvent());
                finish();
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                ToastUtils.show("左2响应菜单");
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                ToastUtils.show("左2响应新建文章");
                break;
        }
    }

    @Override
    public void onTitleRightFirstClick(int mRightFirstIconType) {
        switch (mRightFirstIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                RxBus.INSTANCE.send(new ReloadWebPageEvent());
                finish();
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                ToastUtils.show("右1响应菜单");
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                ToastUtils.show("右1响应新建文章");
                break;
        }
    }

    @Override
    public void onTitleRightSecondClick(int mRightSecondIconType) {
        switch (mRightSecondIconType) {
            case WebTitleHelper.NATIVE_ICON_TYPE_RETURN:
                RxBus.INSTANCE.send(new ReloadWebPageEvent());
                finish();
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_HOME_MENU:
                ToastUtils.show("右2响应菜单");
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_ICON_TYPE_NEW_ARTICLE:
                ToastUtils.show("右2响应新建文章");
                break;
        }
    }
}
