package com.techjumper.lib2.others;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;
import com.techjumper.corelib.utils.bitmap.BitmapUtils;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/4/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PicassoRoundTransform implements Transformation {

    private String mKey;
    private float mStrokeWidth = 1.F;

    public PicassoRoundTransform(String key) {
        mKey = key;
    }

    public PicassoRoundTransform(String key, float strokeWidth) {
        mKey = key;
        mStrokeWidth = strokeWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        return BitmapUtils.toCircle(source, mStrokeWidth);
    }

    @Override
    public String key() {
        return mKey;
    }
}
