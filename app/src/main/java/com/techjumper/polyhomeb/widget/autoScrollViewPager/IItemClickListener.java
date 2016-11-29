package com.techjumper.polyhomeb.widget.autoScrollViewPager;

import com.techjumper.polyhomeb.other.ADVideoHelper;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public interface IItemClickListener {
    void onClick(int position, ADVideoHelper object);

    void onPageSelected(int position, ADVideoHelper mInflate);
}
