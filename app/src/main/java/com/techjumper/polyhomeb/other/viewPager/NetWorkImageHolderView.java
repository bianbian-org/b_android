package com.techjumper.polyhomeb.other.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.other.ADVideoHelper;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.Holder;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.IItemClickListener;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.OnItemClickListener;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NetWorkImageHolderView
        implements Holder<ADEntity.DataBean.AdInfosBean>, ViewPager.OnPageChangeListener
        , OnItemClickListener {

    private ImageView mImageView;
    private ADVideoHelper mHelper;
    private View mInflate;
    private int mCurrentPosition;
    private IItemClickListener mListener;
    private View mItemView;  //当前展示的item

    public NetWorkImageHolderView(IItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public View createView(Context context, ADEntity.DataBean.AdInfosBean data) {

        switch (data.getMedia_type()) {
            case 1:
            default:
                mImageView = new ImageView(context);
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mItemView = mImageView;
                return mImageView;
            case 2:
                mInflate = LayoutInflater.from(context).inflate(R.layout.layout_ad_texture_view, null, false);
                mItemView = mInflate;
                return mInflate;
        }
    }

    @Override
    public void updateUI(Context context, int position, ADEntity.DataBean.AdInfosBean data) {

        mCurrentPosition = position;


        switch (data.getMedia_type()) {
            case 1:
            default:
                if (mImageView == null) {
                    mImageView = new ImageView(context);
                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                String media_url = data.getMedia_url();
                Glide.with(context)
                        .load(TextUtils.isEmpty(media_url) ? R.mipmap.ad_no_ad : Config.sHost + media_url)
                        .placeholder(R.mipmap.ad_place_holder)
                        .into(mImageView);
                break;
            case 2:

                if (mInflate == null)
                    mInflate = LayoutInflater.from(context).inflate(R.layout.layout_ad_texture_view, null, false);
                mHelper = new ADVideoHelper(context, data, mInflate);
                mHelper.startWork();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int index) {
        ToastUtils.show("index" + index + "...." + mCurrentPosition);
        if (mCurrentPosition != index)
            mHelper.stopWork();
    }

    @Override
    public void onItemClick(int position, Object object) {
        mListener.onClick(position, object, mHelper, mItemView);
    }
}