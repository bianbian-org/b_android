package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MyVillageFamilyTextData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MyVillageFamilyTextData.class, beanName = "MyVillageFamilyTextBean")
public class MyVillageFamilyTextViewHolder extends BaseRecyclerViewHolder<MyVillageFamilyTextData> {

    public static final int LAYOUT_ID = R.layout.item_my_village_family_text;

    public MyVillageFamilyTextViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MyVillageFamilyTextData data) {
        if (data == null) return;
        setText(R.id.tv, data.getText());
    }
}
