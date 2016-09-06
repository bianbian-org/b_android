package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
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
        mWebView.addJsInterface(this, Constant.JS_NATIVE_BRIDGE);
        mWebView.processBack();
        mWebView.loadUrl(url);
    }

    @Override
    protected boolean isWebViewActivity() {
        return true;
    }

    @Override
    public void onTitleLeftFirstClick(String mLeftFirstMethod) {
        switch (mLeftFirstMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            default:
                onLineMethod(mLeftFirstMethod);
                break;
        }
    }

    @Override
    public void onTitleLeftSecondClick(String mLeftSecondMethod) {
        switch (mLeftSecondMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            default:
                onLineMethod(mLeftSecondMethod);
                break;
        }
    }

    @Override
    public void onTitleRightFirstClick(String mRightFirstMethod) {
        switch (mRightFirstMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            default:
                onLineMethod(mRightFirstMethod);
                break;
        }
    }

    @Override
    public void onTitleRightSecondClick(String mRightSecondMethod) {
        switch (mRightSecondMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
//                RxBus.INSTANCE.send(new ReloadWebPageEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            default:
                onLineMethod(mRightSecondMethod);
                break;
        }
    }

    /**
     * 根据url传回来的method,调用页面的function
     */
    private void onLineMethod(String method) {
        if (TextUtils.isEmpty(method)) return;
        mWebView.loadUrl("javascript:" + method + "()");
    }

    @Override
    public void onBackPressed() {
//        RxBus.INSTANCE.send(new ReloadWebPageEvent());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) mWebView.destroy();
        super.onDestroy();
    }

    public PolyWebView getWebView() {
        return mWebView;
    }
}
