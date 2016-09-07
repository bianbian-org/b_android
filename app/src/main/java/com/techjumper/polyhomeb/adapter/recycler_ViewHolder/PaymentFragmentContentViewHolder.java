package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lightwidget.textview.MarqueeTextView;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PaymentFragmentContentData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = PaymentFragmentContentData.class, beanName = "PaymentFragmentContentBean")
public class PaymentFragmentContentViewHolder extends BaseRecyclerViewHolder<PaymentFragmentContentData> {

    public static final int LAYOUT_ID = R.layout.item_payment_content;

    public PaymentFragmentContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PaymentFragmentContentData data) {
        if (data == null) return;
        String btnName = "";
        ((MarqueeTextView) getView(R.id.tv_notice)).setText(data.getTitle());
        switch (data.getBtnName()) {
            //1-物业费 2-水费 3-电费 4-燃气费 5-其他
            case 1:
                btnName = getContext().getResources().getString(R.string.pop_property_pay);
                break;
            case 2:
                btnName = getContext().getResources().getString(R.string.pop_water_pay);
                break;
            case 3:
                btnName = getContext().getResources().getString(R.string.pop_elec_pay);
                break;
            case 4:
                btnName = getContext().getResources().getString(R.string.pop_gas_pay);
                break;
            case 5:
                btnName = getContext().getResources().getString(R.string.pop_other);
                break;
        }
        setText(R.id.btn, btnName);
        setText(R.id.tv_content, getContext().getString(R.string.charge_object) + data.getContent());
        setText(R.id.tv_time, String.format(getContext().getString(R.string.pay_death_line), data.getTime()));
        setText(R.id.tv_price, getContext().getString(R.string.￥) + data.getPrice());
        if (data.getDay() < 0) {
            setText(R.id.tv_exceed_day, String.format(getContext().getString(R.string.already_overrun_day), Math.abs(data.getDay())));
        } else if (data.getDay() == 0) {
            setText(R.id.tv_exceed_day, getContext().getString(R.string.surplus_0_day));
        } else {
            setText(R.id.tv_exceed_day, String.format(getContext().getString(R.string.surplus_day), data.getDay()));
        }

        setOnClickListener(R.id.tv_pay_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_num = data.getOrder_num();
                ToastUtils.show("立即付款~~" + "订单号:" + order_num);
            }
        });

    }
}
