package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
public class AdActivityPresenter extends AppBaseActivityPresenter<AdActivity> {

    @OnClick(R.id.bottom_back)
    void back() {
        getView().finish();
    }

    @OnClick(R.id.img)
    void ad() {
        ImageView imageView = getView().getImg();
        WebView webView = getView().getWebView();

        imageView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        webView.loadUrl("http://www.ourjujia.com/");
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
