package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.js.AndroidForJs;
import com.techjumper.polyhome.b.home.mvp.p.activity.ShoppingActivityPresenter;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

@Presenter(ShoppingActivityPresenter.class)
public class ShoppingActivity extends AppBaseActivity<ShoppingActivityPresenter> {

    @Bind(R.id.webview)
    WebView webView;

    @Bind(R.id.bottom_title)
    TextView bottomTitle;
    @Bind(R.id.bottom_date)
    TextView bottomDate;
    private Timer timer = new Timer();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_shopping);
    }

    public TextView getBottomDate() {
        return bottomDate;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomTitle.setText(R.string.title_shopping);
        bottomDate.setText(CommonDateUtil.getTitleDate());

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
        webView.setWebViewClient(new webViewClient());
        webView.loadUrl("http://pl.techjumper.com/shop/pad/login");
        webView.addJavascriptInterface(new AndroidForJs(this), "JavaScriptInterface");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RxBus.INSTANCE.send(new TimeEvent());
            }
        }, CommonDateUtil.delayToPoint(), 60000);
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
