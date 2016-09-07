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
        if (data == null) return;
        mWhere = data.getWhere();
        initPopup();
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

    private void initPopup() {

        //1-物业费 2-水费 3-电费 4-燃气费 5-其他   全部
        mPop = new PolyPopupWindow((Activity) getContext(), R.style.popup_anim, new PopListItemClick(), mRootView, new PopDismiss());
        mPop.setAnchorView(getView(R.id.tv_type));
        mPop.setXoff(110);
        List<String> datas = new ArrayList<>();
        datas.add(getContext().getResources().getString(R.string.pop_property_pay));
        datas.add(getContext().getResources().getString(R.string.pop_water_pay));
        datas.add(getContext().getResources().getString(R.string.pop_elec_pay));
        datas.add(getContext().getResources().getString(R.string.pop_gas_pay));
        datas.add(getContext().getResources().getString(R.string.pop_other));
        datas.add(getContext().getResources().getString(R.string.pop_all));
        mPop.initData(datas);
    }

    private class PopListItemClick implements PolyPopupWindow.ItemClickCallBack {

        @Override
        public void callBack(int position, String s) {
            //1-物业费 2-水费 3-电费 4-燃气费 5-其他(查询全部，则为空)
            switch (position) {
                case 0:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_property_pay));
                    break;
                case 1:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_water_pay));
                    break;
                case 2:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_elec_pay));
                    break;
                case 3:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_gas_pay));
                    break;
                case 4:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_other));
                    break;
                case 5:
                    mPop.thisDismiss(PolyPopupWindow.AnimStyle.ALPHA);
                    setText(R.id.tv_type, getContext().getResources().getString(R.string.pop_all));
                    break;
            }
            RxBus.INSTANCE.send(new PaymentQueryEvent(position, mWhere)); //点击之后发送消息,然后刷新重新调用接口
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
