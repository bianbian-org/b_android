package com.techjumper.polyhomeb.interfaces;

import android.webkit.WebView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IWebViewChromeClient {
    void onPageLoadFinish(WebView view, int newProgress);
}
