package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "NoDataBean", data = NoDataData.class)
public class NoDataViewHolder extends BaseRecyclerViewHolder<NoDataData> {

    public static final int LAYOUT_ID = R.layout.item_no_data;

    public NoDataViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(NoDataData data) {

    }
}
