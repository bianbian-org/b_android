package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.CheckInTitleData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = CheckInTitleData.class, beanName = "CheckInTitleBean")
public class CheckInTitleViewHolder extends BaseRecyclerViewHolder<CheckInTitleData> {

    public static final int LAYOUT_ID = R.layout.item_check_in_title;

    public CheckInTitleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(CheckInTitleData data) {
        if (data == null) return;
        setText(R.id.tv_month, data.getMonth() + "");
        setText(R.id.tv_total, String.format(getContext().getString(R.string.total_check_in), data.getTotal()));
    }
}
