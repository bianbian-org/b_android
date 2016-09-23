package com.techjumper.polyhome.b.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/9/18.
 */

public class AdViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private List<AdEntity.AdsEntity> adsEntities;

    public AdViewPagerAdapter() {
        views = new ArrayList<View>();
    }

    public void setViews(List<View> views, List<AdEntity.AdsEntity> adsEntities) {
        if (views == null || views.size() == 0)
            return;

        this.views = views;
        this.adsEntities = adsEntities;
    }

    public void clear() {
        if (views != null) {
            views.clear();
        }
        if (adsEntities != null) {
            adsEntities.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public void playVideo(View view, File file) {
        ((MyTextureView) view).initMediaPlayer();
        if (file.exists()) {
            Log.d("ad12", "textureView加载本地: " + file.getAbsolutePath());
            ((MyTextureView) view).play(file.getAbsolutePath());
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("ad12", "instantiateItem");
        View view = views.get(position);
        view.setTag(position);

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        if (view instanceof ImageView) {
            Log.d("ad12", "我是imageview");
            AdEntity.AdsEntity entity = adsEntities.get(position);
            File file = entity.getFile();
            if (file.exists()) {
                Log.d("ad12", "image加载本地");
                PicassoHelper.load(file)
                        .noPlaceholder()
                        .noFade()
                        .into((ImageView) view);
            } else {
                Log.d("ad12", "image加载网络");
                PicassoHelper.load(entity.getMedia_url())
                        .noPlaceholder()
                        .noFade()
                        .into((ImageView) view);
            }
        } else {
            Log.d("ad12", "我是textureView");
            ((MyTextureView) view).initMediaPlayer();
        }

        container.addView(view);
        return view;
    }
//
//    @Override
//    public void notifyDataSetChanged() {
//        childCount = getCount();
//        super.notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        if (childCount > 0) {
//            childCount--;
//            return POSITION_NONE;
//        }
//        return super.getItemPosition(object);
//    }
}


