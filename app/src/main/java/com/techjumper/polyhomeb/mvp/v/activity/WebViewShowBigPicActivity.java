package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.PicViewAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.WebViewShowBigPicActivityPresenter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.List;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(WebViewShowBigPicActivityPresenter.class)
public class WebViewShowBigPicActivity extends AppBaseActivity<WebViewShowBigPicActivityPresenter> {

    @Bind(R.id.vp)
    ViewPager mVp;
    @Bind(R.id.indicator)
    MagicIndicator mIndicator;

    private PicViewAdapter mAdapter;
    private int mCurrentUrlPosition = 0;
    private List<String> mUrls;
    private int mIndex;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_web_view_show_big_pic);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19) {
            AcHelper.fullScreen(this, true);
        }

        mUrls = getPresenter().getPicList();
        mIndex = getPresenter().getIndex();

        CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setCircleCount(mUrls.size());
        circleNavigator.setCircleColor(ResourceUtils.getColorResource(R.color.color_37a991));
        circleNavigator.setCircleClickListener(index -> mVp.setCurrentItem(index));
        mIndicator.setNavigator(circleNavigator);

        mAdapter = new PicViewAdapter(Glide.with(this), mUrls);
        mVp.setAdapter(mAdapter);
        if (mUrls.size() > mIndex) {
            mCurrentUrlPosition = mIndex;
        } else {
            mCurrentUrlPosition = 0;
        }
        mVp.setCurrentItem(mCurrentUrlPosition);
        mVp.setOffscreenPageLimit(mUrls.size());
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    protected boolean isWebViewActivity() {
        return true;
    }
}
