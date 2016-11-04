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
public class NetWorkImageHolderView implements Holder<String> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
//        imageView.setImageResource(R.drawable.ic_default_adimage);
//        ImageLoader.getInstance().displayImage(data, imageView);
    }
}
