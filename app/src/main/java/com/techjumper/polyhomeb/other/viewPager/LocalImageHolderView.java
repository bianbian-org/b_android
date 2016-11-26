package com.techjumper.polyhomeb.other.viewPager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.polyhomeb.widget.autoScrollViewPager.Holder;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class LocalImageHolderView implements Holder<Integer> {
    private ImageView imageView;

    @Override
    public View createView(Context context, Integer t) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void updateUI(Context context, int position, Integer data) {
        imageView.setImageResource(data);
    }
}
