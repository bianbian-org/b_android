package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.UnlockVillageTextData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = UnlockVillageTextData.class, beanName = "UnlockVillageTextBean")
public class UnlockVillageTextViewHolder extends BaseRecyclerViewHolder<UnlockVillageTextData> {

    public static final int LAYOUT_ID = R.layout.item_unlock_village_text;

    public UnlockVillageTextViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(UnlockVillageTextData data) {
        if (data == null) return;
        setText(R.id.tv, data.getText());
    }
}
