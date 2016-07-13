package com.techjumper.polyhomeb.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class IndicatorAdapter extends CommonNavigatorAdapter {

    private int mCount;
    private List<String> mIndicatorTitles;
    private ViewPager mViewPager;
    private String mNormalColor,mSelectedColor;

    public IndicatorAdapter(List<String> mIndicatorTitles, ViewPager mViewPager) {
        this.mCount = mIndicatorTitles.size();
        this.mIndicatorTitles = mIndicatorTitles;
        this.mViewPager = mViewPager;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public IPagerTitleView getItemView(Context context, int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setText(mIndicatorTitles.get(index));
        colorTransitionPagerTitleView.setNormalColor(Color.parseColor(mNormalColor));
        colorTransitionPagerTitleView.setSelectedColor(Color.parseColor(mSelectedColor));
        colorTransitionPagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
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

    public void setNormalColor(String color) {
        this.mNormalColor = color;
    }

    public void setSelectedColor(String color) {
        this.mSelectedColor = color;
    }

}
