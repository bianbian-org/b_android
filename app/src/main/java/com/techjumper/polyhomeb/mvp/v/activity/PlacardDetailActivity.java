package com.techjumper.polyhomeb.mvp.v.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.PlacardDetailActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(PlacardDetailActivityPresenter.class)
public class PlacardDetailActivity extends AppBaseActivity<PlacardDetailActivityPresenter> {

    @Bind(R.id.tv_title_)
    TextView mTvTitle;
    @Bind(R.id.btn_type)
    Button mBtnType;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.wb)
    WebView mWebView;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_placard_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init();
        setUpView();
        getPresenter().loading();
    }

    @Override
    public String getLayoutTitle() {
        return String.format(getResources().getString(R.string.placard_detail), getPresenter().getType());
    }

    private void init() {
        mTvTitle.setText(getPresenter().getTitle());
        mBtnType.setText(getPresenter().getType());
        mTvTime.setText(getPresenter().getTime());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpView() {
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setBlockNetworkImage(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }

        mWebView.setWebViewClient(new PolyWebViewClient());
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
    }


    public void onDataReceive(String data) {
        if (TextUtils.isEmpty(data)) return;
        mWebView.loadDataWithBaseURL(null, data, "text/html", "utf-8",
                null);
    }

    class PolyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.requestFocus();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                ToastUtils.show(getResources().getString(R.string.error_no_browser));
                e.printStackTrace();
            }
        }
    }
}
