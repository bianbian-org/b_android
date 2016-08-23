package com.techjumper.polyhomeb.interfaces;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IWebView {
    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

    void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse);

    void onScrollChanged(int l, int t, int oldl, int oldt);
}
