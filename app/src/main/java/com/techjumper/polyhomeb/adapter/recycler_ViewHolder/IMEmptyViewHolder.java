package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.IMEmptyViewData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "IMEmptyViewBean", data = IMEmptyViewData.class)
public class IMEmptyViewHolder extends BaseRecyclerViewHolder<IMEmptyViewData> {

    public static final int LAYOUT_ID = R.layout.item_im_empty;

    public IMEmptyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(IMEmptyViewData data) {

    }
}
