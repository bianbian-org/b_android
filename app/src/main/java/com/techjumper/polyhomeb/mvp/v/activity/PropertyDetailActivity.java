package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.FragmentAdapter;
import com.techjumper.polyhomeb.adapter.IndicatorAdapter;
import com.techjumper.polyhomeb.entity.emqtt.PropertyEmqttUpdateEvent;
import com.techjumper.polyhomeb.mvp.p.activity.PropertyDetailActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.fragment.AppBaseFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.ComplainFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.PlacardFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.RepairFragment;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.widget.NonScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(PropertyDetailActivityPresenter.class)
public class PropertyDetailActivity extends AppBaseActivity<PropertyDetailActivityPresenter> {

    @Bind(R.id.indicator)
    MagicIndicator mIndicator;
    @Bind(R.id.vp)
    NonScrollViewPager mViewPager;
    @Bind(R.id.right_group)
    FrameLayout mRightGroup;

    private int mComeFrom;
    private List<String> mIndicatorTitles = new ArrayList<>();
    private List<AppBaseFragment> mFragments = new ArrayList<>();
    private boolean mHasAuthority = UserManager.INSTANCE.isFamily();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_property_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mComeFrom = getPresenter().comeFromWitchButton();
        initIndicator();
        initViewPager();
        mViewPager.setCanScroll(mHasAuthority);
    }

    @Override
    public String getLayoutTitle() {
        return getResources().getString(R.string.property);
    }

    @Override
    protected boolean showTitleRight() {
        return Constant.VALUE_PLACARD != mComeFrom;  //为了一进入界面的时候show或者hide右边icon
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    private void initIndicator() {
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.placard));
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.repair_));
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.complaint_));
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdjustMode(true);
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(mIndicatorTitles, mViewPager, mHasAuthority);
        indicatorAdapter.setNormalColor("#acacac");
        indicatorAdapter.setSelectedColor("#37a991");
        navigator.setAdapter(indicatorAdapter);
        mIndicator.setNavigator(navigator);
    }

    private void initViewPager() {
        mFragments.add(PlacardFragment.getInstance());
        mFragments.add(RepairFragment.getInstance());
        mFragments.add(ComplainFragment.getInstance());
        FragmentAdapter adapter = new FragmentAdapter(this, mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.onPageSelected(position);
                if (Constant.VALUE_PLACARD != position) {   //当滑动的时候show或者hide右边icon
                    mRightGroup.setVisibility(View.VISIBLE);
                } else {
                    mRightGroup.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mIndicator.onPageScrollStateChanged(state);
            }
        });
        mViewPager.setCurrentItem(mComeFrom);  //如此设置了之后,indicator也会跟着一起被设置
    }

    public NonScrollViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mViewPager == null) return;
        int position = intent.getExtras().getInt(Constant.KEY_CURRENT_BUTTON);
        JLog.d("传递到那个界面  ---->" + position);
        mViewPager.setCurrentItem(position);
        RxBus.INSTANCE.send(new PropertyEmqttUpdateEvent(position));
    }
}
