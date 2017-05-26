package com.techjumper.polyhomeb.mvp.v.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.OrderDetailActivityPresenter;
import com.techjumper.polyhomeb.widget.AdvancedWebView;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(OrderDetailActivityPresenter.class)
public class OrderDetailActivity extends AppBaseActivity<OrderDetailActivityPresenter> {


    @Bind(R.id.root_view)
    LinearLayout mLayout;
    @Bind(R.id.tv_title)
    TextView mTitle;

    private AdvancedWebView mWebView;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_order_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initWebView();
    }

    @JavascriptInterface
    private void initWebView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new AdvancedWebView(this);
        mWebView.setLayoutParams(params);
        mLayout.addView(mWebView);

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(false);
        mWebSettings.setLoadWithOverviewMode(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadsImagesAutomatically(true);

        mWebSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
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

        loadUrl();
    }

    private void loadUrl() {
        if (!TextUtils.isEmpty(getPresenter().getObjId())) {
            String url = Config.sHost + "/shop/mobile/msg/order?order_no=" + getPresenter().getObjId();
            mWebView.loadUrl(url);
        }
    }

    public void showTitle(String title) {
        mTitle.setText(TextUtils.isEmpty(title) ? getString(R.string.jujia_item_name) : title);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
