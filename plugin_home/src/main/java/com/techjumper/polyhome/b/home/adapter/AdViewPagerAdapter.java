package com.techjumper.polyhome.b.home.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.Space;
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
    public static final String IMAGE_AD_TYPE = "1";
    public static final String VIDEO_AD_TYPE = "2";

    private List<AdEntity.AdsEntity> adsEntities = new ArrayList<>();

    public AdViewPagerAdapter() {

    }

    //    public void setViews(List<View> views, List<AdEntity.AdsEntity> adsEntities) {
//        this.views.clear();
//        if (views != null && views.size() != 0)
//            this.views.addAll(views);
//        this.adsEntities.clear();
//        if (adsEntities != null && adsEntities.size() != 0)
//            this.adsEntities.addAll(adsEntities);
//        notifyDataSetChanged();
//    }
    public void setDatas(List<AdEntity.AdsEntity> adsEntities) {
        this.adsEntities.clear();
        if (adsEntities != null && adsEntities.size() != 0)
            this.adsEntities.addAll(adsEntities);
        notifyDataSetChanged();
    }

    public void clear() {
        if (adsEntities != null) {
            adsEntities.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adsEntities.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

//        container.removeView(views.get(position));
        container.removeView((View) object);
    }

    public void playVideo(View view, File file) {
        if (!(view instanceof MyTextureView))
            return;
        ((MyTextureView) view).initMediaPlayer();
        if (file.exists()) {
            Log.d("ad12", "textureView加载本地: " + file.getAbsolutePath());
            ((MyTextureView) view).play(file.getAbsolutePath());
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        if (views == null || views.size() <= position)
//            return null;
        Log.d("ad12", "instantiateItem");
        View resultView = null;
        {
            AdEntity.AdsEntity entity = adsEntities.get(position);
            File file = entity.getFile();
            String adType = entity.getMedia_type();
            Log.d("ad12", file + ", 详细信息: " + entity);

//            if (adImageView == null || textureView == null)
//                return;
            if (IMAGE_AD_TYPE.equals(adType)) {
                resultView = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.layout_ad_image, null);
                if (file.exists())
                    entity.setMedia_url(file.getAbsolutePath());
            } else if (VIDEO_AD_TYPE.equals(adType)) {
                resultView = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_ad_video, null);
                if (file.exists())
                    entity.setMedia_url(file.getAbsolutePath());
            }
        }
        if (resultView == null)
            resultView = new Space(container.getContext());
        resultView.setTag(position);
        ViewGroup parent = (ViewGroup) resultView.getParent();
        if (parent != null) {
            parent.removeView(resultView);
        }

        if (resultView instanceof ImageView) {
            Log.d("ad12", "我是imageview");
            AdEntity.AdsEntity entity = adsEntities.get(position);
            File file = entity.getFile();
            if (file.exists()) {
                Log.d("ad12", "image加载本地");
                PicassoHelper.load(file)
                        .noPlaceholder()
                        .noFade()
                        .into((ImageView) resultView);
            } else {
                Log.d("ad12", "image加载网络");
                PicassoHelper.load(entity.getMedia_url())
                        .noPlaceholder()
                        .noFade()
                        .into((ImageView) resultView);
            }
        } else if (resultView instanceof MyTextureView) {
            Log.d("ad12", "我是textureView");
            ((MyTextureView) resultView).initMediaPlayer();
        }

        container.addView(resultView);
        return resultView;
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


