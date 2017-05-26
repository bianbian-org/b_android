package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.BlackViewData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = BlackViewData.class, beanName = "BlackViewBean")
public class BlackViewHolder extends BaseRecyclerViewHolder<BlackViewData> {

    public static final int LAYOUT_ID = R.layout.black_view;

    public BlackViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(BlackViewData data) {

    }
}
