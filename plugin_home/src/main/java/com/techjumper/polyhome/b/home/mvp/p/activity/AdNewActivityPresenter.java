package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdNewActivity;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
public class AdNewActivityPresenter extends AppBaseActivityPresenter<AdNewActivity> {

    @OnClick(R.id.bottom_back)
    void back() {
        getView().finish();
    }

//
//    @OnClick(R.id.img)
//    void ad() {
//        if (getView().getAdType() == PloyhomeFragmentPresenter.IMAGE_AD_TYPE) {
//            AdEntity.AdsEntity adsEntity = getView().getAdsEntity();
//            if (adsEntity == null || TextUtils.isEmpty(adsEntity.getUrl()))
//                return;
//
//            ImageView imageView = getView().getImg();
//            WebView webView = getView().getWebView();
//
//            imageView.setVisibility(View.GONE);
//            webView.setVisibility(View.VISIBLE);
//
//            webView.loadUrl(adsEntity.getUrl());
//        }
//    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
    }
}
