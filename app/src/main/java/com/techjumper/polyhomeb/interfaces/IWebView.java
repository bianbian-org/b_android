package com.techjumper.polyhomeb.interfaces;

import android.webkit.WebView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IWebView {
    void onError(WebView view, int errorCode, String description, String failingUrl);

    void onScrollChanged(int l, int t, int oldl, int oldt);
}
