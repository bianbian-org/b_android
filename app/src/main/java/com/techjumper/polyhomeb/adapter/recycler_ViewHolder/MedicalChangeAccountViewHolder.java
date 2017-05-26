package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalChangeAccountData;
import com.techjumper.polyhomeb.entity.event.MedicalChangeAccountEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MedicalChangeAccountData.class, beanName = "MedicalChangeAccountBean")
public class MedicalChangeAccountViewHolder extends BaseRecyclerViewHolder<MedicalChangeAccountData> {

    public static final int LAYOUT_ID = R.layout.item_medical_change_account;

    public MedicalChangeAccountViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MedicalChangeAccountData data) {
        if (data == null) return;
        ImageView view = getView(R.id.iv_choose);
        setText(R.id.tv_name, data.getName());
        if (data.isCurrentAccount()) {
            PicassoHelper.getDefault().load(R.mipmap.icon_choose_green).into(view);
        } else {
            view.setImageBitmap(null);
        }
        setOnClickListener(R.id.layout_root, v -> RxBus.INSTANCE.send(new MedicalChangeAccountEvent(data.getUserId())));
    }
}
