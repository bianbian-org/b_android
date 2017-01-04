package com.techjumper.polyhomeb.mvp.v.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.manager.WebTitleManager;
import com.techjumper.polyhomeb.mvp.p.activity.JSInteractionActivityPresenter;
import com.techjumper.polyhomeb.utils.WebTitleHelper;
import com.techjumper.polyhomeb.widget.AdvancedWebView;
import com.techjumper.polyhomeb.widget.PolyWebView;

import butterknife.Bind;

import static com.techjumper.polyhomeb.utils.WebTitleHelper.NATIVE_METHOD_DELETE_ARTICLE;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(JSInteractionActivityPresenter.class)
public class JSInteractionActivity extends AppBaseWebViewActivity<JSInteractionActivityPresenter>
        implements IWebViewTitleClick {

    @Bind(R.id.left_first_iv)
    ImageView iv;
    @Bind(R.id.tv_title)
    TextView mTitle;

    private WebTitleManager mWebTitleManager;
    private String mUrl = "";

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_js_interaction);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        initWebView((AdvancedWebView) findViewById(R.id.wb));
        getWebView().addJsInterface(this, Constant.JS_NATIVE_BRIDGE);

        //如果广告url不为null，那么证明是从广告来的
        if (!TextUtils.isEmpty(getPresenter().getADUrl())) {
            mUrl = getPresenter().getADUrl();
            getWebView().setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    showTitle(view.getTitle());
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }
            });
        } else {
            mUrl = getPresenter().getUrl();
        }
        mWebTitleManager = new WebTitleManager(mUrl, mViewRoot, this);
        getWebView().loadUrl(mUrl);
    }

    private void showTitle(String title) {
        mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
    }

    @Override
    public PolyWebView getWebView() {
        return (PolyWebView) super.getWebView();
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
            case NATIVE_METHOD_DELETE_ARTICLE:
                getPresenter().deleteArticle();
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

    @Override
    public void onResume() {
        super.onResume();
        if (mWebTitleManager == null)
            mWebTitleManager = new WebTitleManager(mUrl, mViewRoot, this);
        if (!TextUtils.isEmpty(mWebTitleManager.getPageName())
                && mWebTitleManager.getPageName().equalsIgnoreCase("mypl")) {
            //说明这个页面是商城的 "我的" 页面
            //刷新当前页面，使页面数据更新
            getWebView().reload();
        }
    }

    /**
     * 根据url传回来的method,调用页面的function
     */
    public void onLineMethod(String method) {
        if (TextUtils.isEmpty(method)) return;
        getWebView().loadUrl("javascript:" + method + "()");
    }

    /**
     * 用于支付之后，通知H5支付结束
     */
    public void refreshH5StateEvent(String order_number) {
        if (TextUtils.isEmpty(order_number)) return;
        getWebView().loadUrl("javascript:" + "refresh_order(" + order_number + ")");
    }

    @Override
    public void onBackPressed() {
        if (mWebTitleManager == null)
            mWebTitleManager = new WebTitleManager(mUrl, mViewRoot, this);
        if (!TextUtils.isEmpty(mWebTitleManager.getPageReturn())
                && mWebTitleManager.getPageReturn().equalsIgnoreCase("home")) {
            //如果在商城"结算"界面，那么返回直接回到商城首页
            new AcHelper.Builder(this).closeCurrent(true).target(TabHomeActivity.class).start();
        } else {
            //如果不在商城的"结算"界面，那么返回操作就是默认处理方式
            super.onBackPressed();
        }
    }
}
