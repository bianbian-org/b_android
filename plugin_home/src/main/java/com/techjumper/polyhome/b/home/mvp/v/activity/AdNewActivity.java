package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.techjumper.commonres.ComConstant;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.adapter.AdViewPagerAdapter;
import com.techjumper.polyhome.b.home.db.util.AdClickDbUtil;
import com.techjumper.polyhome.b.home.mvp.p.activity.AdNewActivityPresenter;
import com.techjumper.polyhome.b.home.widget.AdViewPager;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.manager.AdController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
@Presenter(AdNewActivityPresenter.class)
public class AdNewActivity extends AppBaseActivity<AdNewActivityPresenter> implements AdController.IAlarm, ViewPager.OnPageChangeListener {
    public static final String IMAGE_AD_TYPE = "1";
    public static final String VIDEO_AD_TYPE = "2";
    public static final String TIME = "time";

    public static final String POSITION = "position";

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;

    public static String TYPE = "type";

    private long time;
    private int position;
    private boolean isFirst = true;
    private int type = TYPE_ONE;
    private String typeString = AdController.TYPE_HOME;
    private float x1, x2, y1, y2;

    private AdController adController;
    private AdViewPagerAdapter adapter = new AdViewPagerAdapter();
    private List<View> views = new ArrayList<>();
    private int currentPage = 0;
    private LayoutInflater inflater;
    private View currentView;
    private List<AdEntity.AdsEntity> adsEntities = new ArrayList<>();
    private String addType = IMAGE_AD_TYPE;
    private AdEntity.AdsEntity mAdsEntity;
    private Timer timer = new Timer();

    @Bind(R.id.pager)
    AdViewPager adViewPager;

    @Bind(R.id.webview)
    WebView webView;

    @Bind(R.id.call_layout)
    LinearLayout call;

    @OnClick(R.id.bottom_back)
    void back() {
        if (webView.getVisibility() == View.VISIBLE) {
            adViewPager.setVisibility(View.VISIBLE);
            call.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.layout_ad_new);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        position = getIntent().getIntExtra(POSITION, 0);
        Log.d("ad15", "过来的position" + position);
        type = getIntent().getIntExtra(TYPE, TYPE_ONE);
        time = getIntent().getLongExtra(TIME, 0L);

        call.setVisibility(View.VISIBLE);

        if (time == 0L) {
            time = System.currentTimeMillis() / 1000;
        }

        setTime(time);

        inflater = LayoutInflater.from(this);

        adController = AdController.getInstance();
        adController.startPolling(this, true);

        adViewPager.setAdapter(adapter);
        adViewPager.setOffscreenPageLimit(3);
        adViewPager.addOnPageChangeListener(this);

        adViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        adController.stopAdTimer(AdController.TYPE_HOME);
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();
                        if (Math.abs(x1 - x2) < 6 && Math.abs(y1 - y2) < 6) {
                            if (adsEntities != null && adsEntities.size() > 0) {
                                AdEntity.AdsEntity adsEntity = adsEntities.get(adViewPager.getCurrentItem());
                                if (adsEntity != null && !TextUtils.isEmpty(adsEntity.getUrl())) {
                                    adViewPager.setVisibility(View.GONE);
                                    call.setVisibility(View.GONE);
                                    webView.setVisibility(View.VISIBLE);

                                    webView.loadUrl(adsEntity.getUrl());
                                }
                            }
                        }
                        adController.startAdTimer(AdController.TYPE_HOME, currentPage);
                        break;
                }
                return false;
            }
        });

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time != 0L) {
                    time = time + 1;
                    setTime(time);
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onPause() {
//        if (adapter != null && adViewPager != null) {
//            Log.d("ad12", "清除");
//            adViewPager.removeAllViews();
//            adViewPager.setAdapter(adapter = new AdViewPagerAdapter());
//        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (adController != null) {
            adController.clearPolling();
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onAlarmReceive(boolean fromCache) {
        if (type == TYPE_ONE) {
            typeString = AdController.TYPE_HOME;
        } else {
            typeString = AdController.TYPE_HOME_TWO;
        }

        adController.executeAdRule(typeString, UserInfoManager.getFamilyId()
                , UserInfoManager.getUserId(), UserInfoManager.getTicket()
                , fromCache
                , new AdController.IExecuteRule() {

                    @Override
                    public void onAllAdsReceive(List<AdEntity.AdsEntity> allAds) {
                        if (allAds == null || allAds.size() == 0)
                            return;

                        Log.d("ad13", allAds + "");

                        if (views != null && views.size() > 0) {
                            views.clear();
                        }

                        if (adsEntities != null && adsEntities.size() > 0) {
                            adsEntities.clear();
                        }
                        for (int i = 0; i < allAds.size(); i++) {
                            AdEntity.AdsEntity entity = allAds.get(i);
                            File file = entity.getFile();
                            if (file.exists()) {
                                adsEntities.add(entity);
                                Log.d("ad12", file + ", 详细信息: " + entity);
                                addType = entity.getMedia_type();

//                                if (adImageView == null || textureView == null)
//                                    return;
                                if (AdViewPagerAdapter.IMAGE_AD_TYPE.equals(addType)) {
//                                    ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_ad_image, null);
                                    View imageView = new Space(AdNewActivity.this);

                                    views.add(imageView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                } else if (AdViewPagerAdapter.VIDEO_AD_TYPE.equals(addType)) {
//                                    MyTextureView textureView = (MyTextureView) inflater.inflate(R.layout.layout_ad_video, null);
                                    View textureView = new Space(AdNewActivity.this);

                                    views.add(textureView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                }
                            }
                        }
//                        adapter.setViews(views, adsEntities);
//                        adapter.setDatas(adsEntities);
//                        if (adViewPager == null)
//                            return;
//                        if (adViewPager.getWrapper() != null) {
//                            adViewPager.getWrapper().notifyDataSetChanged();
//                        }
                        if (adViewPager == null)
                            return;
                        adapter = new AdViewPagerAdapter();
                        adapter.setDatas(adsEntities);
                        adViewPager.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                        if (isFirst) {
                            if (position <= views.size() - 1) {
                                currentPage = position;
                            } else {
                                currentPage = 0;
                            }
                            adViewPager.setCurrentItem(currentPage, false, true);
                            mAdsEntity = adsEntity;
                            Log.d("ad15", "第一次page:" + currentPage);
                            isFirst = false;
                        } else {
                            Log.d("ad15", "currentPage: " + currentPage);
                            adViewPager.setCurrentItem(currentPage, false, true);

                            if (currentPage == views.size() - 1) {
                                currentPage = -1;
                            }
                            currentPage++;
                        }
                    }

                    @Override
                    public void onAdPlayFinished() {

                    }

                    @Override
                    public void onAdDownloadError(AdEntity.AdsEntity adsEntity) {

                    }

                    @Override
                    public void onAdExecuteFailed(String reason) {

                    }

                    @Override
                    public void onAdNoExist(String adType, String hour) {

                    }
                });

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
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (views.size() != 0
                && adsEntities.size() != 0
                && views.size() == adsEntities.size()) {
            mAdsEntity = adsEntities.get(position);
            if (mAdsEntity.getMedia_type().equals(VIDEO_AD_TYPE)) {
                if (currentView != null) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
                currentView = adViewPager.findViewWithTag(position);

                if (currentView == null)
                    return;

                adapter.playVideo(currentView, mAdsEntity.getFile());
            } else {
                if (currentView != null && currentView instanceof MyTextureView) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
            }
        }
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2 && mAdsEntity != null) {
            AdClickDbUtil.insert(Long.valueOf(mAdsEntity.getId()), AdController.TYPE_HOME, ComConstant.AD_TYPE_SLIDE, time);
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
//            dismissLoading();
            super.onPageFinished(view, url);
        }
    }
}
