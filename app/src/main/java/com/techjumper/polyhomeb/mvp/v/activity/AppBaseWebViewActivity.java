package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.techjumper.polyhomeb.mvp.p.activity.AppBaseActivityPresenter;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.widget.AdvancedWebView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/9/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public abstract class AppBaseWebViewActivity<T extends AppBaseActivityPresenter> extends AppBaseActivity<T>
        implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView;
    private boolean mIsInit;
    private String mTitle;

    protected void initWebView(AdvancedWebView webView) {
        mWebView = webView;
        mWebView.setListener(this, this);
        mWebView.setGeolocationEnabled(false);
        mWebView.setMixedContentAllowed(true);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle = title;
                AppBaseWebViewActivity.this.onReceivedTitle(view, title);
            }

        });
        addHttpHeaders();
        mIsInit = true;
    }

    public String getUrlTitle() {
        return mTitle;
    }

    public AdvancedWebView getWebView() {
        if (!mIsInit)
            throw new NullPointerException("no call initWebView()");
        return mWebView;
    }

    public void addHttpHeaders() {
        mWebView.addHttpHeader("HUSERID", UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));
        mWebView.addHttpHeader("HTICKET", UserManager.INSTANCE.getTicket());
        mWebView.addHttpHeader("HVILLAGEID", UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        if (UserManager.INSTANCE.isFamily()) {
            mWebView.addHttpHeader("HFAMILYID", UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID));
        } else {
            mWebView.addHttpHeader("HFAMILYID", "");
        }
    }

    public void removeHttpHeaders() {
        mWebView.removeHttpHeader("HUSERID");
        mWebView.removeHttpHeader("HTICKET");
        mWebView.removeHttpHeader("HVILLAGEID");
        mWebView.removeHttpHeader("HFAMILYID");
    }

    public boolean webViewIsInit() {
        return mWebView != null && mIsInit;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webViewIsInit()) {
            mWebView.onResume();
        }
        // ...
    }

    @Override
    public void onPause() {
        if (webViewIsInit()) {
            mWebView.onPause();
        }
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webViewIsInit()) {
            mWebView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (webViewIsInit()) {
            mWebView.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onBackPressed() {
//        if (!mWebView.onBackPressed()) {
//            return;
//        }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {
//        JLog.e("onPageFinished");
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
//        JLog.e("onPageError:errorCode->" + errorCode);
    }

    @Override
    public void onPageHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//        JLog.e("onPageHttpError:errorResponse.getStatusCode->" + errorResponse.getStatusCode());
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    protected void onReceivedTitle(WebView view, String title) {

    }
}
