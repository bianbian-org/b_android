package com.techjumper.polyhomeb.other;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.steve.creact.library.display.DisplayBean;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CustomGridLayoutManager extends GridLayoutManager {

    private ViewTreeObserver obs;
    private List<DisplayBean> list;
    private MyOnPreDrawListener listener;

    public CustomGridLayoutManager(List<DisplayBean> list, Context context, int spanCount, final RecyclerView recyclerView) {
        super(context, spanCount);
        this.list = list;
        obs = recyclerView.getViewTreeObserver();
        listener = new MyOnPreDrawListener(recyclerView);
        obs.addOnPreDrawListener(listener);
    }

    private void calculateRecyclerViewFullHeight(RecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getChildCount() == 0) return;
        int height = recyclerView.getChildAt(0).getHeight();
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        if (list.size() > 8) {
            params.height = height * 4;
            recyclerView.setLayoutParams(params);
        } else {
            params.height = recyclerView.getMeasuredHeight();
            recyclerView.setLayoutParams(params);
        }
        if (listener != null) {
//            obs.removeOnPreDrawListener(listener);
        }
    }

    private class MyOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

        private RecyclerView recyclerView;

        public MyOnPreDrawListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onPreDraw() {
            calculateRecyclerViewFullHeight(recyclerView);
            return true;
        }
    }
}
