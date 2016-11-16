package com.techjumper.polyhomeb.mvp.v.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setBlockNetworkImage(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(false);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);

        mWebView.setWebViewClient(new PolyWebViewClient());
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
    }

    public void onDataReceive(String data) {
        if (TextUtils.isEmpty(data)) return;
        String tpl = getFromAssets("notice.html");
        String web = tpl;
        if (tpl.contains("$content$")) {
            web = tpl.replace("$content$", data);
        }
        mWebView.loadDataWithBaseURL(null, web, "text/html", "utf-8", null);
    }

    private String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
