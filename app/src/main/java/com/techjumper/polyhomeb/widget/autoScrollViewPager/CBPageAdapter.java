package com.techjumper.polyhomeb.widget.autoScrollViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.techjumper.polyhomeb.widget.ADVideoLayout;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.ADEntity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CBPageAdapter extends PagerAdapter {

    private Context context;
    protected List<ADEntity.DataBean.AdInfosBean> mDatas;
    private boolean canLoop = true;
    private CBLoopViewPager viewPager;
    private final int MULTIPLE_COUNT = 300;

    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        int realPosition = position % realCount;
        return realPosition;
    }

    public List<ADEntity.DataBean.AdInfosBean> getDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = toRealPosition(position);
        View view = null;
        switch (mDatas.get(realPosition).getMedia_type()) {
            case 1:
                ImageView mImageView = new ImageView(context);
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                String media_url = mDatas.get(realPosition).getMedia_url();
                Glide.with(context)
                        .load(TextUtils.isEmpty(media_url) ? R.mipmap.ad_no_ad : Config.sHost + media_url)
                        .placeholder(R.mipmap.ad_place_holder)
                        .into(mImageView);
                view = mImageView;
                break;
            case 2:
                ADVideoLayout layout = new ADVideoLayout(context);
                layout.setData(mDatas.get(realPosition));
                layout.initVideo();
                view = layout;
                break;
        }
        view.setTag(realPosition);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFirstItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public void setViewPager(CBLoopViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public CBPageAdapter(Context context, List<ADEntity.DataBean.AdInfosBean> datas) {
        this.context = context;
        this.mDatas = datas;
    }

}
