package com.techjumper.polyhomeb.widget.autoScrollViewPager;

import android.content.Context;
import android.view.View;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface Holder<T> {
    View createView(Context context, T t);

    void UpdateUI(Context context, int position, T data);
}
