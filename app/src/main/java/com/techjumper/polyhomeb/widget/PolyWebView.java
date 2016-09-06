package com.techjumper.polyhomeb.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.techjumper.polyhomeb.interfaces.IWebView;
import com.techjumper.polyhomeb.interfaces.IWebViewChromeClient;
import com.techjumper.polyhomeb.other.JavascriptObject;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PolyWebView extends WebView {

    private IWebViewChromeClient iWebViewChromeClient;
    private IWebView iWebView;
    private Context mContext;

    public PolyWebView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PolyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public PolyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        WebSettings mWebSettings = getSettings();
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setBlockNetworkImage(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setWebViewClient(new PolyWebViewClient());
        setWebChromeClient(new PolyWebChromeClient());
        setOnLongClickListener(v -> true);//屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件:
    }

    public void addJsInterface(Activity activity, String clazzName) {
        addJavascriptInterface(new JavascriptObject(activity), clazzName);
    }

    public void processBack() {
        setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {  //表示按返回键 时的操作
                    goBack();   //后退
                    return true;    //已处理
                }
            }
            return false;
        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (iWebView != null) {
            iWebView.onScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnWebViewChromeClientListener(IWebViewChromeClient iWebViewChromeClient) {
        this.iWebViewChromeClient = iWebViewChromeClient;
    }

    public void setOnWebViewReceiveErrorListener(IWebView iWebView) {
        this.iWebView = iWebView;
    }

//    private WebResourceResponse method(String url) {
//
//        try {
//
//            OkHttpClient httpClient = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url(url.trim())
//                    .header("User-Agent", "OkHttp Headers.java")
//                    .addHeader("huserid", UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID))
//                    .addHeader("hticket", UserManager.INSTANCE.getTicket())
//                    .build();
//
//            return new WebResourceResponse("text/html", "utf-8"
//                    , httpClient.newCall(request).execute().body().byteStream());
//
//        } catch (IOException e) {
//            return null;
//        }
//    }

    private class PolyWebViewClient extends WebViewClient {

//        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            if (request.getUrl().toString().contains(".css")) {
//                return super.shouldInterceptRequest(view, request);
//            } else {
//                return method(request.getUrl().toString());
//            }
//        }

//        @SuppressWarnings("deprecation")
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            if (url.contains(".css")) {
//                return super.shouldInterceptRequest(view, url);
//            } else {
//                return method(url);
//            }
//        }

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
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (iWebView != null) {
                iWebView.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if (iWebView != null) {
                iWebView.onReceivedHttpError(view, request, errorResponse);
            }
        }
    }

    private class PolyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (100 == newProgress) {
                if (iWebViewChromeClient != null) {
                    iWebViewChromeClient.onPageLoadFinish(view, newProgress);
                }
            }
        }
    }
}
