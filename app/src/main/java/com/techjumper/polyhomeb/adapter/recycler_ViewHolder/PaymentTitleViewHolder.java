package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PaymentTitleData;
import com.techjumper.polyhomeb.entity.PaymentTypeEntity;
import com.techjumper.polyhomeb.entity.event.PaymentQueryEvent;
import com.techjumper.polyhomeb.widget.PolyPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PaymentTitleBean", data = PaymentTitleData.class)
public class PaymentTitleViewHolder extends BaseRecyclerViewHolder<PaymentTitleData> {

    public static final int LAYOUT_ID = R.layout.item_payment_title;

    private PolyPopupWindow mPop;
    private View mRootView;
    private ImageView mIvTriangle;

    private int mWhere = 0;

    public PaymentTitleViewHolder(View itemView) {
        super(itemView);
        mRootView = getView(R.id.tv_type);
        mIvTriangle = getView(R.id.iv_triangle);
    }

    @Override
    public void setData(PaymentTitleData data) {
        if (data == null || data.getItems() == null || data.getItems().size() == 0) return;
        mWhere = data.getWhere();
        List<PaymentTypeEntity.DataBean.ItemsBean> items = data.getItems();

        initPopup(items);
        setText(R.id.tv_title, data.getTitle());
        setText(R.id.tv_total, String.format(getContext().getString(R.string.payment_yuan), data.getTotal()));
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

    private void initPopup(List<PaymentTypeEntity.DataBean.ItemsBean> items) {
        mPop = new PolyPopupWindow((Activity) getContext(), R.style.popup_anim, new PopListItemClick(items)
                , mRootView, new PopDismiss());
        mPop.setAnchorView(getView(R.id.tv_type));
        mPop.setXoff(110);
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            PaymentTypeEntity.DataBean.ItemsBean itemsBean = items.get(i);
            String item_name = itemsBean.getItem_name();
            datas.add(item_name);
        }
        mPop.initData(datas);
    }

    private class PopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        private List<PaymentTypeEntity.DataBean.ItemsBean> items;

        public PopListItemClick(List<PaymentTypeEntity.DataBean.ItemsBean> items) {
            this.items = items;
        }

        @Override
        public void callBack(int position, String s) {
            for (int i = 0; i < items.size(); i++) {
                PaymentTypeEntity.DataBean.ItemsBean itemsBean = items.get(i);
                String item_name = itemsBean.getItem_name();
                if (item_name.equalsIgnoreCase(s)) {
                    int id = itemsBean.getId();
                    RxBus.INSTANCE.send(new PaymentQueryEvent(id, mWhere)); //点击之后发送消息,然后刷新重新调用接口
                    setText(R.id.tv_type, s);
                    break;
                }
            }
        }
    }

    private class PopDismiss implements PolyPopupWindow.OnPopDismiss {
        @Override
        public void onDismiss() {
            android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(mIvTriangle, "rotation", 90f, 0f);
            animator.setDuration(300);
            animator.start();
        }
    }
}
