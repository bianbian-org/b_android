package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyComplainTitleData;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyComplainTitleBean", data = PropertyComplainTitleData.class)
public class PropertyComplainTitleViewHolder extends BaseRecyclerViewHolder<PropertyComplainTitleData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_title;
    private PolyPopupWindow mPop;
    private View mRootView;
    private ImageView mIvTriangle;

    public PropertyComplainTitleViewHolder(View itemView) {
        super(itemView);
        mRootView = getView(R.id.tv_type);
        mIvTriangle = getView(R.id.iv_triangle);
    }

    @Override
    public void setData(PropertyComplainTitleData data) {
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
        mPop.setMarginTop(55);
        List<String> datas = new ArrayList<>();
        datas.add(getContext().getResources().getString(R.string.repair_all));
        datas.add(getContext().getResources().getString(R.string.pop_commited));
        datas.add(getContext().getResources().getString(R.string.pop_reply));
        datas.add(getContext().getResources().getString(R.string.pop_done));
        mPop.initData(datas);
    }

    private class PopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        // TODO: 16/7/17  点击了对应的item之后,应该按照那个item的协议来对数据进行过滤

        @Override
        public void callBack(int position,String s) {
            switch (position) {
                case 0:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    ToastUtils.show("点击了全部");
                    setText(R.id.tv_type, "全部");
                    break;
                case 1:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    ToastUtils.show("点击了已提交");
                    setText(R.id.tv_type, "已提交");
                    break;
                case 2:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    ToastUtils.show("点击了已回复");
                    setText(R.id.tv_type, "已回复");
                    break;
                case 3:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    ToastUtils.show("点击了已完成");
                    setText(R.id.tv_type, "已完成");
                    break;
            }
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
