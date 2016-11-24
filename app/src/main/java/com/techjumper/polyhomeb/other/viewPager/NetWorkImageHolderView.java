package com.techjumper.polyhomeb.other.viewPager;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.Holder;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NetWorkImageHolderView implements Holder<ADEntity.DataBean.AdInfosBean> {

    private ImageView mImageView;
    private TextureView mTextureView;


    @Override
    public View createView(Context context, ADEntity.DataBean.AdInfosBean data) {
        switch (data.getMedia_type()) {
            case 1:
            default:
                mImageView = new ImageView(context);
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return mImageView;
            case 2:
                mTextureView = new TextureView(context);
                return mTextureView;
        }
    }

    @Override
    public void UpdateUI(Context context, int position, ADEntity.DataBean.AdInfosBean data) {
        switch (data.getMedia_type()) {
            case 1:
            default:
                String media_url = data.getMedia_url();
                Glide.with(context)
                        .load(TextUtils.isEmpty(media_url) ? R.mipmap.ad_no_ad : Config.sHost + media_url)
                        .placeholder(R.mipmap.ad_place_holder)
                        .into(mImageView);
                break;
            case 2:
                break;
        }
    }
}
