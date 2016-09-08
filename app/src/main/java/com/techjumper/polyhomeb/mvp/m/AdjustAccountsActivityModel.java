package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.AdjustAccountsActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdjustAccountsActivityModel extends BaseModel<AdjustAccountsActivityPresenter> {

    public AdjustAccountsActivityModel(AdjustAccountsActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    /**
     * 订单号 334209320948023
     */
    public String getOrderNum() {
        return getExtra().getString(Constant.KEY_ORDER_NUMBER, "");
    }

    /**
     * 费用名称  2月水费
     */
    public String getPayName() {
        return getExtra().getString(Constant.KEY_PAY_NAME, "");
    }

    /**
     * 收费对象  2栋2楼202
     */
    public String getPayObj() {
        return getExtra().getString(Constant.KEY_PAY_OBJECT, "");
    }

    /**
     * 截止日期 2016-6-6
     */
    public String getDeathLine() {
        return getExtra().getString(Constant.KEY_PAY_DEATH_LINE, "");
    }

    /**
     * 总价(不包括滞纳金)
     */
    public double getTotal() {
        return getExtra().getDouble(Constant.KEY_PAY_TOTAL, 0);
    }

    /**
     * 滞纳金
     */
    public double getExpiryPrice() {
        return getExtra().getDouble(Constant.KEY_PAY_EXPIRY, 0);
    }

    /**
     * 费用类型  1-物业费 2-水费 3-电费 4-燃气费 5-其他
     */
    public int getPayType() {
        return getExtra().getInt(Constant.KEY_PAY_TYPE, 0);
    }

    /**
     * //是否逾期 0-没逾期, 1-逾期
     */
    public int getIsLate() {
        return getExtra().getInt(Constant.KEY_PAY_IS_LATE, 0);
    }

    /**
     * 超过X天,还剩下10天,是+10天,逾期超过了10天则是-10天
     */
    public int getDay() {
        return getExtra().getInt(Constant.KEY_PAY_DAY, 0);
    }

    /**
     * 费用类型  1-物业费 2-水费 3-电费 4-燃气费 5-其他
     */
    public String getPayTypeString() {
        switch (getPayType()) {
            case 1:
                return getPresenter().getView().getString(R.string.pop_property_pay);
            case 2:
                return getPresenter().getView().getString(R.string.pop_water_pay);
            case 3:
                return getPresenter().getView().getString(R.string.pop_elec_pay);
            case 4:
                return getPresenter().getView().getString(R.string.pop_gas_pay);
            case 5:
                return getPresenter().getView().getString(R.string.pop_other);
        }
        return "";
    }

}
