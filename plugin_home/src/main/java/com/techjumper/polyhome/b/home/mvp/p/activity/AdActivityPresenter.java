package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
public class AdActivityPresenter extends AppBaseActivityPresenter<AdActivity> {

    @OnClick(R.id.bottom_back)
    void back() {
        getView().finish();
    }

    private ImageView imageView;
    private MyTextureView textureView;

    @OnClick(R.id.img)
    void ad() {
        if (getView().getAdType() == PloyhomeFragmentPresenter.IMAGE_AD_TYPE) {
            AdEntity.AdsEntity adsEntity = getView().getAdsEntity();
            if (adsEntity == null || TextUtils.isEmpty(adsEntity.getUrl()))
                return;

            ImageView imageView = getView().getImg();
            WebView webView = getView().getWebView();

            imageView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            webView.loadUrl(adsEntity.getUrl());
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (textureView != null) {
            textureView.initMediaPlayer();
            textureView.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (textureView != null) {
            textureView.pause();
        }
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        AdEntity.AdsEntity adsEntity = getView().getAdsEntity();
//        if (adsEntity == null || TextUtils.isEmpty(adsEntity.getUrl()))
//            return;

        if (getView().getAdType().equals(PloyhomeFragmentPresenter.VIDEO_AD_TYPE)) {
            imageView = getView().getImg();
            textureView = getView().getTextureView();

            textureView.setOnStateChangeListener(new MyTextureView.OnStateChangeListener() {
                @Override
                public void onSurfaceTextureDestroyed(SurfaceTexture surface) {

                }

                @Override
                public void onBuffering() {

                }

                @Override
                public void onPlaying() {

                }

                @Override
                public void onStart() {
                    imageView.setVisibility(View.INVISIBLE);
                    textureView.play(adsEntity.getMedia_url());
                }

                @Override
                public void onSeek(int max, int progress) {

                }

                @Override
                public void onStop() {

                }
            });

        }
    }
}
