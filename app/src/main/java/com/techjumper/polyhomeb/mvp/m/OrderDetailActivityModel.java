package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.p.activity.OrderDetailActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class OrderDetailActivityModel extends BaseModel<OrderDetailActivityPresenter> {

    public OrderDetailActivityModel(OrderDetailActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getObjId() {
        return getExtra().getString(Constant.KEY_ORDER_MESSAGE_ID, "");
    }

    public int getOrderId() {
        return getExtra().getInt(Constant.KEY_ORDER_ID, 0);
    }

    public Observable<TrueEntity> updateMessage() {
        KeyValuePair keyValuePair = KeyValueCreator.updateMessageState(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getOrderId() + "");
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault().updateMessageState(entity)
                .compose(CommonWrap.wrap());

    }
}
