package com.techjumper.polyhome.b.property.viewholder;

import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.PayEntity;
import com.techjumper.commonres.entity.event.PayShowEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;

/**
 * Created by kevin on 16/11/11.
 */
@DataBean(beanName = "PayEntityBean", data = PayEntity.PayItemEntity.class)
public class PayViewHolder extends BaseRecyclerViewHolder<PayEntity.PayItemEntity> {

    public static final int LAYOUT_ID = R.layout.item_pay;

    public PayViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PayEntity.PayItemEntity data) {
        if (data == null)
            return;

        int status = data.getStatus();
        setText(R.id.name, data.getPay_name());

        if (status == PayEntity.NO) {
            setText(R.id.deadline, getContext().getString(R.string.property_pay_deadline_list) + data.getExpiry_date());
            if (data.getIs_late() == PayEntity.OVER_NO) {
                setText(R.id.date_state, String.format(getContext().getString(R.string.pay_remain_day), String.valueOf(Math.abs(data.getExpiry()))));
            } else {
                setText(R.id.date_state, String.format(getContext().getString(R.string.pay_over_day), String.valueOf(Math.abs(data.getExpiry()))));
            }
            setVisibility(R.id.pay, View.VISIBLE);
        } else {
            setText(R.id.deadline, getContext().getText(R.string.property_pay_date) + data.getPayment_date());
            if (data.getIs_late() == PayEntity.OVER_NO) {
                setText(R.id.date_state, getContext().getString(R.string.pay_normal));
            } else {
                setText(R.id.date_state, String.format(getContext().getString(R.string.pay_over_day_price), String.valueOf(Math.abs(data.getExpiry())), data.getExpiry_price()));
            }
            setVisibility(R.id.pay, View.GONE);
        }

        TextView typeTextView = getView(R.id.status_type);
        if (status == Constant.PAY_PROPERTY) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_359321);
            typeTextView.setText(R.string.pay_property);
        } else if (status == Constant.PAY_WATER) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_0ca9e4);
            typeTextView.setText(R.string.pay_water);
        } else if (status == Constant.PAY_ELECTRIC) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_900ce4);
            typeTextView.setText(R.string.pay_electric);
        } else if (status == Constant.PAY_GAS) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_e4830c);
            typeTextView.setText(R.string.pay_gas);
        } else {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_b7d412);
            typeTextView.setText(R.string.pay_else);
        }

        setText(R.id.object, getContext().getString(R.string.property_pay_object) + data.getObject());
        setText(R.id.price, "￥" + data.getPrice());

        setOnClickListener(R.id.pay, v -> {
            RxBus.INSTANCE.send(new PayShowEvent(data));
        });
    }
}
