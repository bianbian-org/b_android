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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.JujiaDetailWebActivityPresenter;
import com.techjumper.polyhomeb.other.JavascriptObject;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.widget.AdvancedWebView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(JujiaDetailWebActivityPresenter.class)
public class JujiaDetailWebActivity extends AppBaseActivity<JujiaDetailWebActivityPresenter> {

    @Bind(R.id.close_group)
    FrameLayout mLayoutClose;
    @Bind(R.id.back_group)
    FrameLayout mLayoutBack;
    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.refresh_group)
    FrameLayout mLayoutRefresh;
    @Bind(R.id.root_view)
    LinearLayout mLayout;

    private AdvancedWebView mWebView;

    private final Map<String, String> mHttpHeaders = new HashMap<>();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_jujia_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mLayoutClose.setVisibility(View.GONE);
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
        mWebView.addJavascriptInterface(new JavascriptObject(this), Constant.JS_NATIVE_BRIDGE);

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
                canGoBack(view.canGoBack());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        addHeaders();
        loadUrl();
    }

    public AdvancedWebView getWebView() {
        return mWebView;
    }

    private void addHeaders() {
        mHttpHeaders.put("HUSERID", UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));
        mHttpHeaders.put("HTICKET", UserManager.INSTANCE.getTicket());
        mHttpHeaders.put("HVILLAGEID", UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        if (UserManager.INSTANCE.isFamily()) {
            mHttpHeaders.put("HFAMILYID", UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID));
        } else {
            mHttpHeaders.put("HFAMILYID", "");
        }
    }

    private void loadUrl() {
        if (!TextUtils.isEmpty(getPresenter().getJumpUrl())) {
            mWebView.loadUrl(getPresenter().getJumpUrl(), mHttpHeaders);
        }
    }

    public void canGoBack(boolean canGoBack) {
        if (canGoBack)
            mLayoutClose.setVisibility(View.VISIBLE);
        else
            mLayoutClose.setVisibility(View.GONE);
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
