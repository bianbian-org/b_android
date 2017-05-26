package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.CheckInWeekData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = CheckInWeekData.class, beanName = "CheckInWeekBean")
public class CheckInWeekViewHolder extends BaseRecyclerViewHolder<CheckInWeekData> {

    //下面的日期和这个星期不共用的原因是,日期的文字大小不同,这里还要单独设置,麻烦,所以针对31个日期,单独写个item
    public static final int LAYOUT_ID = R.layout.item_check_in_week;

    public CheckInWeekViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(CheckInWeekData data) {
        if (data == null) return;
    }
}
