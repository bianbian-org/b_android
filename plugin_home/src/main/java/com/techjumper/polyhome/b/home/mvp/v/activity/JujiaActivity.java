package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.js.AndroidForJs;
import com.techjumper.polyhome.b.home.mvp.p.activity.JujiaActivityPresenter;
import com.techjumper.polyhome_b.adlib.Config;

import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

@Presenter(JujiaActivityPresenter.class)
public class JujiaActivity extends AppBaseActivity<JujiaActivityPresenter> {

    public static final String TIME = "time";

    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.bottom_title)
    TextView bottomTitle;
    @Bind(R.id.bottom_date)
    TextView bottomDate;
    @Bind(R.id.close)
    TextView close;
    @Bind(R.id.bottom_back)
    LinearLayout bottomBack;
    @Bind(R.id.call_text)
    TextView callText;
    @Bind(R.id.call_layout)
    LinearLayout callLayout;

    private long time;
    private TimerTask timerTask;

    public TextView getBottomDate() {
        return bottomDate;
    }

    public WebView getWebView() {
        return webView;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_jujia);
    }

    public long getTime() {
        return time;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomTitle.setText(R.string.title_jujia_server);
        time = getIntent().getLongExtra(TIME, 0L);
        close.setVisibility(View.VISIBLE);

        callText.setText(R.string.call_order);

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
                    if (webView.getUrl().equals(Config.sJujia)) {
                        callLayout.setVisibility(View.GONE);
                    } else {
                        callLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    // 加载中
                }

            }
        });
        webView.setWebViewClient(new webViewClient());
        webView.loadUrl(Config.sJujia);
        webView.addJavascriptInterface(new AndroidForJs(this), "JavaScriptInterface");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            showLoading();
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            dismissLoading();
        }
    }
}
