package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.activity.AdDetailActivityPresenter;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.window.AdWindowManager;

import butterknife.Bind;

@Presenter(AdDetailActivityPresenter.class)
public class AdDetailActivity extends AppBaseActivity<AdDetailActivityPresenter> {

    public static final String ADITEM = "aditem";
    private AdEntity.AdsEntity adsEntity = new AdEntity.AdsEntity();

    @Bind(R.id.webview)
    WebView webView;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_ad_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            adsEntity = (AdEntity.AdsEntity) getIntent().getSerializableExtra(ADITEM);
        }

        if (adsEntity == null || TextUtils.isEmpty(adsEntity.getUrl()))
            return;

        WebSettings ws = webView.getSettings();

        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setAppCacheEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        ws.setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成

                } else {
                    // 加载中

                }

            }
        });
        webView.setWebViewClient(new AdDetailActivity.webViewClient());
        webView.loadUrl(adsEntity.getUrl());

    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }

    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            showLoading();
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dismissLoading();
            super.onPageFinished(view, url);
        }
    }
}
