package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairPhotoViewChoosedData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "NewRepairPhotoViewChoosedBean", data = NewRepairPhotoViewChoosedData.class)
public class NewRepairPhotoViewChoosedViewHolder extends BaseRecyclerViewHolder<NewRepairPhotoViewChoosedData> {

    public static final int LAYOUT_ID = R.layout.item_new_repair_photo_view_default_plus;

    public NewRepairPhotoViewChoosedViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(NewRepairPhotoViewChoosedData data) {
        if (data == null) return;
        ((ImageView) getView(R.id.iv)).setImageBitmap(data.getChoosed());
//        getView(R.id.iv_delete).setVisibility(View.VISIBLE);
//        setOnClickListener(R.id.iv_delete, v -> {
//
//        });
    }
}
