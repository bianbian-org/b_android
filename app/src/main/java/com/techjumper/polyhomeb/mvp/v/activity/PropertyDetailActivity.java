package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.PropertyDetailActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.fragment.AppBaseFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.FriendFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.ShoppingFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

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
    ViewPager mViewPager;

    private int mComeFrom;
    private String[] mIndicatorTitles = new String[]{"维修", "投诉", "公告"};
    private List<AppBaseFragment> mFragments = new ArrayList<>();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_property_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mComeFrom = getPresenter().comeFromWitchButton();
        initIndicator();
        initViewPager();
    }

    @Override
    public String getLayoutTitle() {
//        switch (mComeFrom) {
//            case Constant.VALUE_REPAIR:
//                return getResources().getString(R.string.repair);
//            case Constant.VALUE_COMPLAINT:
//                return getResources().getString(R.string.complaint);
//            case Constant.VALUE_PLACARD:
//                return getResources().getString(R.string.placard);
//        }
//        return "";
        return getResources().getString(R.string.property);
    }

    private void initIndicator() {
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdjustMode(true);
        navigator.setAdapter(new IndicatorAdapter());
        mIndicator.setNavigator(navigator);
    }

    private void initViewPager() {
        HomeFragment homeFragment = new HomeFragment();
        FriendFragment friendFragment = new FriendFragment();
        ShoppingFragment shoppingFragment = new ShoppingFragment();

        mFragments.add(homeFragment);
        mFragments.add(friendFragment);
        mFragments.add(shoppingFragment);

        FragmentAdapter adapter = new FragmentAdapter();

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private class IndicatorAdapter extends CommonNavigatorAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public IPagerTitleView getItemView(Context context, int index) {
            ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
            colorTransitionPagerTitleView.setText(mIndicatorTitles[index]);
            colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#acacac"));
            colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#37A991"));
            colorTransitionPagerTitleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
            return colorTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setLineHeight(UIUtil.dip2px(context, 2));
            indicator.setRoundRadius(UIUtil.dip2px(context, 0));
            List<String> colorList = new ArrayList<>();
            colorList.add("#37A991");
            indicator.setColorList(colorList);
            return indicator;
        }
    }

    private class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
