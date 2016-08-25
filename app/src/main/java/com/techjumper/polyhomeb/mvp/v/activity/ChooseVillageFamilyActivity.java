package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.ChooseVillageFamilyActivityAdapter;
import com.techjumper.polyhomeb.adapter.VillageAdapter;
import com.techjumper.polyhomeb.entity.event.VillageEntity;
import com.techjumper.polyhomeb.mvp.p.activity.ChooseVillageFamilyActivityPresenter;
import com.techjumper.polyhomeb.other.SpaceItemDecoration;
import com.techjumper.polyhomeb.utils.TitleHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ChooseVillageFamilyActivityPresenter.class)
public class ChooseVillageFamilyActivity extends AppBaseActivity<ChooseVillageFamilyActivityPresenter> {

    @Bind(R.id.indicator)
    MagicIndicator mIndicator;  //指示器
    @Bind(R.id.tv_all_area)
    TextView mTvAllArea;        //指示器不显示的时候,显示的文字  所有区域
    @Bind(R.id.layout_triangle)
    FrameLayout mLayoutTriangle; //三角形所在的布局,用来点击的布局
    @Bind(R.id.iv_triangle)
    ImageView mIvTriangle;      //三角形图标
    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.vp)
    ViewPager mVp;              //用来配合indicator使用的
    @Bind(R.id.layout_villages)
    LinearLayout mLayoutVillages; //具体小区的布局
    @Bind(R.id.rv_villages)
    RecyclerViewFinal mRvVillages;//具体小区的Rv

    @Bind(R.id.view_divider_small)
    View mDividerSmall;
    @Bind(R.id.view_divider_big)
    View mDividerBig;

    private List<String> mIndicatorTitles = new ArrayList<>();
    private List<VillageEntity.DataBean.InfosBean.VillagesBean> mVillages = new ArrayList<>();
    public static int sCurrentIndex = 0;
    private ChooseVillageFamilyActivityAdapter mAdapter;
    private VillageAdapter mVillageAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        View view = inflate(R.layout.activity_choose_village_family);
        TitleHelper.setTitleRightIcon(view, R.mipmap.icon_scan_code);
        return view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initIndicator();
        initVp();
        initRv();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.choose_village_family);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean showTitleLeft() {
        return false;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
    }

    public void onProvinceDataReceive(List<String> provinces) {
        mIndicatorTitles.addAll(provinces);
//        initIndicator();
//        initVp();
//        initRv();
    }

    public void onNameAndIdDataReceive(List<VillageEntity.DataBean.InfosBean.VillagesBean> namesAndIds) {
        mVillages.addAll(namesAndIds);
    }

    private void initIndicator() {
        /************假数据****************/
        mIndicatorTitles.add("张三");
        mIndicatorTitles.add("李四");
        mIndicatorTitles.add("王麻子");
        mIndicatorTitles.add("张");
        mIndicatorTitles.add("dssd");
        mIndicatorTitles.add("问v");
        mIndicatorTitles.add("232");
        mIndicatorTitles.add("二个如果");
        mIndicatorTitles.add("239y82f");
        mIndicatorTitles.add("fef");
        /************假数据****************/
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setFollowTouch(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mIndicatorTitles == null ? 0 : mIndicatorTitles.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mIndicatorTitles.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#727272"));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#37a991"));
                colorTransitionPagerTitleView.setOnClickListener(v -> {
                    mVp.setCurrentItem(index);
                    sCurrentIndex = index;
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                List<String> colorList = new ArrayList<>();
                colorList.add("#37a991");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        mIndicator.setNavigator(commonNavigator);
    }

    private void initVp() {
        mVp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mIndicatorTitles.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                TextView textView = new TextView(container.getContext());
                textView.setText(mIndicatorTitles.get(position));
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.TRANSPARENT);
                container.addView(textView);
                return textView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
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

    private void initRv() {
        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        mRv.addItemDecoration(new SpaceItemDecoration(40, 50));
        mAdapter = new ChooseVillageFamilyActivityAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getRvProvinceDatas());

        mRvVillages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mVillageAdapter = new VillageAdapter();
        mRvVillages.setAdapter(mVillageAdapter);
        mVillageAdapter.loadData(getPresenter().getRvVillageDatas());
    }

    public void processIndicatorAndText(boolean state) {
        mIndicator.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
        mTvAllArea.setVisibility(!state ? View.VISIBLE : View.INVISIBLE);
    }

    public RecyclerViewFinal getRv() {
        return mRv;
    }

    public TextView getTvAllArea() {
        return mTvAllArea;
    }

    public MagicIndicator getIndicator() {
        return mIndicator;
    }

    public ImageView getIvTriangle() {
        return mIvTriangle;
    }

    public ChooseVillageFamilyActivityAdapter getAdapter() {
        return mAdapter;
    }

    public ViewPager getVp() {
        return mVp;
    }

    public View getDividerSmall() {
        return mDividerSmall;
    }

    public View getDividerBig() {
        return mDividerBig;
    }

}
