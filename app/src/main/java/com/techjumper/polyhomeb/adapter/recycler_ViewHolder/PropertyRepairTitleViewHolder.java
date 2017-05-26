package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairTitleData;
import com.techjumper.polyhomeb.entity.event.RepairStatusEvent;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairTitleBean", data = PropertyRepairTitleData.class)
public class PropertyRepairTitleViewHolder extends BaseRecyclerViewHolder<PropertyRepairTitleData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_title;
    private PolyPopupWindow mPop;
    private View mRootView;
    private ImageView mIvTriangle;

    public PropertyRepairTitleViewHolder(View itemView) {
        super(itemView);
        mRootView = getView(R.id.tv_type);
        mIvTriangle = getView(R.id.iv_triangle);
    }

    @Override
    public void setData(PropertyRepairTitleData data) {
        if (data == null) return;
        initPopup();
        setText(R.id.tv_title, data.getTitle());
        setText(R.id.tv_num, data.getCount() + "");
        setOnClickListener(R.id.layout_choose_type, v -> {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mIvTriangle, "rotation", 0f, 90f);
            animator.setDuration(300);
            animator.start();
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            mPop.show(PolyPopupWindow.AnimStyle.ALPHA);
        });
    }

    private void initPopup() {

        mPop = new PolyPopupWindow((Activity) getContext(), R.style.popup_anim, new PopListItemClick(), mRootView, new PopDismiss());
        mPop.setMarginRight(8);
        mPop.setMarginTop(28);
        List<String> datas = new ArrayList<>();
        datas.add(getContext().getResources().getString(R.string.pop_not_process));
        datas.add(getContext().getResources().getString(R.string.pop_reply));
        datas.add(getContext().getResources().getString(R.string.pop_processed));
        datas.add(getContext().getResources().getString(R.string.pop_closed));
        datas.add(getContext().getResources().getString(R.string.pop_all));
        mPop.initData(datas);
    }

    private class PopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            //#0-未处理 1-已回复 2-已处理 3-已关闭(查询全部，则为空)
            switch (position) {
                case 0:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_not_process));  //未处理
                    break;
                case 1:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_reply));//已回复
                    break;
                case 2:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_processed));//已处理
                    break;
                case 3:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_closed)); //已关闭
                    break;
                case 4:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_all));  //全部
                    break;
            }
            RxBus.INSTANCE.send(new RepairStatusEvent(position)); //点击之后发送消息,然后刷新重新调用接口
        }
    }

    private class PopDismiss implements PolyPopupWindow.OnPopDismiss {

        @Override
        public void onDismiss() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mIvTriangle, "rotation", 90f, 0f);
            animator.setDuration(300);
            animator.start();
        }
    }
}
