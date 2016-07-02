package com.techjumper.polyhomeb.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AutoScrollerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Integer> mDataList = new ArrayList<>();
    private List<ImageView> mImageList = new ArrayList<>();
    private View.OnClickListener mClickListener;
    private int length;

    public AutoScrollerAdapter(Context context, List<Integer> list, View.OnClickListener listener) {
        this.mContext = context;
        this.mClickListener = listener;
        setData(list);
    }

    public int getLength() {
        return length;
    }

    private void setData(List<Integer> data) {
        mImageList.clear();
        mDataList.clear();
        if (data != null && data.size() != 0) {
            mDataList.addAll(data);
        }
        int itemCount = data == null ? 0 : data.size();

        // 主要是解决当item为小于3个的时候滑动有问题，这里将其拼凑成3个以上
        if (itemCount < 1) {// 当item个数0
            length = 0;
            mImageList.add(getImageView(0));
            mImageList.add(getImageView(0));
            mImageList.add(getImageView(0));
        } else if (itemCount < 2) { // 当item个数为1
            length = 1;
            mImageList.add(getImageView(0));
            mImageList.add(getImageView(0));
            mImageList.add(getImageView(0));
        } else if (itemCount < 3) {// 当item个数为2
            length = 2;
            mImageList.add(getImageView(0));
            mImageList.add(getImageView(1));
            mImageList.add(getImageView(0));
            mImageList.add(getImageView(1));
        } else {
            for (int i = 0; i < data.size(); i++) {
                mImageList.add(getImageView(i));
            }
            length = mImageList.size();
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = null;
        if (mImageList.size() > 0) {
            iv = mImageList.get(position % mImageList.size());
            if (container.equals(iv.getParent())) {
                container.removeView(iv);
            }
            container.addView(iv);
        }
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    private ImageView getImageView(int position) {
        ImageView iv = new ImageView(mContext);

        if (mDataList.size() > 0 && mClickListener != null) {
            iv.setOnClickListener(mClickListener);
        }
        iv.setImageResource(mDataList.get(position));
        return iv;
    }
}
