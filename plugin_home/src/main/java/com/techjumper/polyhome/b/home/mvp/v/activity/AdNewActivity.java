package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.adapter.AdViewPagerAdapter;
import com.techjumper.polyhome.b.home.mvp.p.activity.AdActivityPresenter;
import com.techjumper.polyhome.b.home.mvp.p.activity.AdNewActivityPresenter;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.widget.AdViewPager;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;
import com.techjumper.polyhome_b.adlib.manager.AdController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin on 16/4/29.
 */
@Presenter(AdNewActivityPresenter.class)
public class AdNewActivity extends AppBaseActivity<AdActivityPresenter> implements AdController.IAlarm, ViewPager.OnPageChangeListener {
    public static final String IMAGE_AD_TYPE = "1";
    public static final String VIDEO_AD_TYPE = "2";

    public static final String POSITION = "position";

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;

    public static String TYPE = "type";

    private int position;
    private boolean isFirst = true;
    private int type = TYPE_ONE;
    private String typeString = AdController.TYPE_HOME;

    private AdController adController;
    private AdViewPagerAdapter adapter;
    private List<View> views = new ArrayList<>();
    private int currentPage = 0;
    private LayoutInflater inflater;
    private View currentView;
    private List<AdEntity.AdsEntity> adsEntities = new ArrayList<>();
    private String addType = IMAGE_AD_TYPE;

    @Bind(R.id.pager)
    AdViewPager adViewPager;

    @OnClick(R.id.call)
    void call() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.CallingActivity");
        it.setComponent(componentName);
        it.putExtra("com.dnake.talk", "CallingActivity");
        startActivity(it);
    }

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.layout_ad_new);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        position = getIntent().getIntExtra(POSITION, 0);
        type = getIntent().getIntExtra(TYPE, TYPE_ONE);
        inflater = LayoutInflater.from(this);

        adController = AdController.getInstance();
        adController.startPolling(this, true);

        adViewPager.setAdapter(adapter = new AdViewPagerAdapter());
        adViewPager.setOffscreenPageLimit(3);
        adViewPager.addOnPageChangeListener(this);
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
        super.onDestroy();
    }

    @Override
    public void onAlarmReceive(boolean fromCache) {
        if (type == TYPE_ONE){
            typeString = AdController.TYPE_HOME;
        }else {
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

                                if (IMAGE_AD_TYPE.equals(addType)) {
                                    ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_ad_image, null);

                                    views.add(imageView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                } else if (VIDEO_AD_TYPE.equals(addType)) {

                                    MyTextureView textureView = (MyTextureView) inflater.inflate(R.layout.layout_ad_video, null);

                                    views.add(textureView);
                                    entity.setMedia_url(file.getAbsolutePath());
                                }
                            }
                        }
                        adapter.setViews(views, adsEntities);
                        adapter.notifyDataSetChanged();
                        if (isFirst) {
                            if (position <= views.size() - 1) {
                                currentPage = position;
                            } else {
                                currentPage = 0;
                            }
                            adViewPager.setCurrentItem(currentPage);
                            isFirst = false;
                        }
                    }

                    @Override
                    public void onAdReceive(AdEntity.AdsEntity adsEntity, File file) {
                        if (currentPage == views.size() - 1) {
                            currentPage = -1;
                        }
                        currentPage++;
                        adViewPager.setCurrentItem(currentPage, false);
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
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (views.size() != 0
                && adsEntities.size() != 0
                && views.size() == adsEntities.size()) {
            AdEntity.AdsEntity adsEntity = adsEntities.get(position);
            if (adsEntity.getMedia_type().equals(VIDEO_AD_TYPE)) {
                if (currentView != null) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
                currentView = adViewPager.findViewWithTag(position);

                if (currentView == null)
                    return;

                adapter.playVideo(currentView, adsEntity.getFile());
            } else {
                if (currentView != null) {
                    ((MyTextureView) currentView).stop();
                    currentView = null;
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
