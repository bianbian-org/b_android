package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.FragmentAdapter;
import com.techjumper.polyhomeb.adapter.IndicatorAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.MessageCenterActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.fragment.AppBaseFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageAllFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageFriendFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageOrdersFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.MessagePropertyFragment;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageSystemFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MessageCenterActivityPresenter.class)
public class MessageCenterActivity extends AppBaseActivity<MessageCenterActivityPresenter> {

    @Bind(R.id.indicator)
    MagicIndicator mIndicator;
    @Bind(R.id.vp)
    ViewPager mViewPager;

    private List<String> mIndicatorTitles = new ArrayList<>();
    private List<AppBaseFragment> mFragments = new ArrayList<>();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_message_center);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initIndicator();
        initViewPager();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.message_center);
    }

    private void initIndicator() {
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.message_title_all));
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.message_title_system));
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.message_title_orders));
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.message_title_friend));
        mIndicatorTitles.add(Utils.appContext.getResources().getString(R.string.message_title_property));
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdjustMode(true);
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(mIndicatorTitles, mViewPager, true);
        indicatorAdapter.setNormalColor("#acacac");
        indicatorAdapter.setSelectedColor("#37a991");
        navigator.setAdapter(indicatorAdapter);
        mIndicator.setNavigator(navigator);
    }

    private void initViewPager() {
        mFragments.add(MessageAllFragment.getInstance());
        mFragments.add(MessageSystemFragment.getInstance());
        mFragments.add(MessageOrdersFragment.getInstance());
        mFragments.add(MessageFriendFragment.getInstance());
        mFragments.add(MessagePropertyFragment.getInstance());
        FragmentAdapter adapter = new FragmentAdapter(this, mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
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
        mViewPager.setCurrentItem(0);
    }
}
