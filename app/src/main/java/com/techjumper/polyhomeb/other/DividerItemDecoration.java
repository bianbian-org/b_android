package com.techjumper.polyhomeb.other;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public DividerItemDecoration(int mSpace) {
        this.mSpace = mSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildPosition(view) != 0)
            outRect.top = mSpace;
        if (parent.getChildPosition(view) == 1) outRect.top = 0;  //viewpager和下一个的交界处没有间距
    }
}
