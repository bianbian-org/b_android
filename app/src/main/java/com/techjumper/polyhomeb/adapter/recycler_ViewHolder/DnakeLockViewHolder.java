package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.DnakeLockData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = DnakeLockData.class, beanName = "DnakeLockBean")
public class DnakeLockViewHolder extends BaseRecyclerViewHolder<DnakeLockData> {

    public static final int LAYOUT_ID = R.layout.item_unlock_village;

    public DnakeLockViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(DnakeLockData data) {
        if (data == null) return;
        setText(R.id.tv_name, data.getName());

    }
}
