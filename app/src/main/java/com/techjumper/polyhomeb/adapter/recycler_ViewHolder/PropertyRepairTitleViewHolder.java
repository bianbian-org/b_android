package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.animation.ObjectAnimator;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairTitleData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairTitleBean", data = PropertyRepairTitleData.class)
public class PropertyRepairTitleViewHolder extends BaseRecyclerViewHolder<PropertyRepairTitleData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_title;

    public PropertyRepairTitleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairTitleData data) {
        if (data == null) return;
        setText(R.id.tv_title, data.getTitle());
        setText(R.id.tv_num, data.getCount() + "");
        View view = getView(R.id.iv_triangle);
        setOnClickListener(R.id.layout_choose_type, v -> {
            //旋转动画
            //弹出窗口
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 90f);
            animator.setDuration(300);
            animator.start();

        });

        //点击了弹出窗口之后
        //窗口消失
        //旋转动画
        //设置文字  setText(R.id.tv_type, 选择的type);
        //发送RxBus,通知adapter按照type类型排序过滤显示

//        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 90f);
//        animator.setDuration(150);
//        animator.start();
    }
}
