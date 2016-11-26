package com.techjumper.polyhomeb.widget.autoScrollViewPager;

import android.view.View;

import com.techjumper.polyhomeb.other.ADVideoHelper;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public interface IItemClickListener {
    void onClick(int position, Object object, ADVideoHelper mHelper, View mItemView);
}
