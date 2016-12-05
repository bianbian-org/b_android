package com.techjumper.polyhomeb.mvp.v.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.PlacardDetailActivityPresenter;
import com.techjumper.polyhomeb.widget.AdvancedWebView;

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
    AdvancedWebView mWebView;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_placard_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void setUp(String content) {
        setUpView(content);
        mTvTitle.setText(getPresenter().getTitle());
        mBtnType.setText(getPresenter().getType());
        mTvTime.setText(getPresenter().getTime());
    }

    @Override
    public String getLayoutTitle() {
        if (getPresenter().getType().equals("0")) {
            return getString(R.string.placard_detail_);
        }
        return String.format(getResources().getString(R.string.placard_detail), getPresenter().getType());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpView(String content) {
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:loadContent('" + content + "')");
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        mWebView.loadUrl("file:///android_asset/notice.html");
    }
}
