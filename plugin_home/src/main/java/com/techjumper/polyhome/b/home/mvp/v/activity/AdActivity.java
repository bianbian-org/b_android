package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.activity.AdActivityPresenter;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.widget.MyVideoView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/4/29.
 */
@Presenter(AdActivityPresenter.class)
public class AdActivity extends AppBaseActivity<AdActivityPresenter> {

    public static final String ADITEM = "aditem";
    private AdEntity.AdsEntity adsEntity = new AdEntity.AdsEntity();

    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.video)
    MyVideoView video;

    private String adType;

    public WebView getWebView() {
        return webView;
    }

    public ImageView getImg() {
        return img;
    }

    public AdEntity.AdsEntity getAdsEntity() {
        return adsEntity;
    }

    public String getAdType() {
        return adType;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.layout_ad);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            adsEntity = (AdEntity.AdsEntity) getIntent().getSerializableExtra(ADITEM);
            adType = adsEntity.getMedia_type();
        }

        if (adType.equals(PloyhomeFragmentPresenter.IMAGE_AD_TYPE)) {

            PicassoHelper.load(new File(adsEntity.getMedia_url()))
                    .noFade()
                    .noPlaceholder()
                    .into(img);

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
        } else if (adType.equals(PloyhomeFragmentPresenter.VIDEO_AD_TYPE)) {
            img.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            video.setVideoURI(Uri.parse(adsEntity.getMedia_url()));
            video.start();
            video.requestFocus();
        }
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
            showLoading();
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dismissLoading();
            super.onPageFinished(view, url);
        }
    }
}
