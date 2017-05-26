package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRoomData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "NewRoomBean", data = NewRoomData.class)
public class NewRoomViewHolder extends BaseRecyclerViewHolder<NewRoomData> {

    public static final int LAYOUT_ID = R.layout.new_room;

    public NewRoomViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(NewRoomData data) {
        if (data == null) return;
        setVisibility(R.id.root, data.isCanShow() ? View.VISIBLE : View.GONE);
    }
}
