package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

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

    private static final int sUnPaid = 1;
    private static final int sAllPaid = 2;

    private static final int sLate = 1; //已经逾期
    private static final int sNotLate = 2;  //未逾期

    public PaymentFragmentContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PaymentFragmentContentData data) {
        if (data == null) return;

        if (sUnPaid == data.getStatus()) {
            getView(R.id.tv_pay_detail).setVisibility(View.GONE);
            getView(R.id.tv_exceed_day).setVisibility(View.VISIBLE);
            getView(R.id.tv_pay_now).setVisibility(View.VISIBLE);
            unPaid(data);
        } else if (sAllPaid == data.getStatus()) {
            getView(R.id.tv_pay_detail).setVisibility(View.VISIBLE);
            getView(R.id.tv_exceed_day).setVisibility(View.GONE);
            getView(R.id.tv_pay_now).setVisibility(View.GONE);
            allPaid(data);
        }

        setText(R.id.tv_price, getContext().getString(R.string.￥) + data.getPrice());
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

        setOnClickListener(R.id.layout_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("查看费用详情");
            }
        });
    }

    private void unPaid(PaymentFragmentContentData data) {
        setText(R.id.tv_time, String.format(getContext().getString(R.string.pay_death_line), data.getTime()));
        if (data.getDay() < 0) {
            setText(R.id.tv_exceed_day, String.format(getContext().getString(R.string.already_overrun_day), Math.abs(data.getDay())));
        } else if (data.getDay() == 0) {
            setText(R.id.tv_exceed_day, getContext().getString(R.string.surplus_0_day));
        } else {
            setText(R.id.tv_exceed_day, String.format(getContext().getString(R.string.surplus_day), data.getDay()));
        }
        setOnClickListener(R.id.tv_pay_now, v -> {
            String order_num = data.getOrder_num();
            ToastUtils.show("立即付款~~" + "订单号:" + order_num);
        });
    }

    private void allPaid(PaymentFragmentContentData data) {
        setText(R.id.tv_time, String.format(getContext().getString(R.string.pay_day), data.getTime()));
        if (sLate == data.getIs_late()) {   //已经逾期
            String text1 = String.format(getContext().getString(R.string.late_x_day), data.getDay()); //超过X天
            String text2 = getContext().getString(R.string.x_exceed);  //产生
            String text3 = getContext().getString(R.string.yuan);  //元
            String text4 = getContext().getString(R.string.expiry_price);  //滞纳金
            TextView tv = getView(R.id.tv_pay_detail);
            tv.setText(Html.fromHtml(text1 + text2 + "<font color='#ff7f00'>"
                    + data.getExpiry_price() + text3 + "</font>") + text4);
        } else if (sNotLate == data.getIs_late()) {  //未逾期
            setText(R.id.tv_pay_detail, getContext().getString(R.string.pay_normal));
        }
    }
}
