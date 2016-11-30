package com.techjumper.polyhome.b.property.mvp.m;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AlipayEntity;
import com.techjumper.commonres.entity.PayEntity;
import com.techjumper.commonres.entity.WxpayEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.fragment.PayFragmentPresenter;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/11/9.
 */

public class PayFragmentModel extends BaseModel<PayFragmentPresenter> {
    public PayFragmentModel(PayFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<PayEntity> getOrders(int status, int page) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getOrders(NetHelper.createBaseArgumentsMap(KeyValueCreator.getOrders(UserInfoManager.getUserId()
                        , UserInfoManager.getFamilyId(), UserInfoManager.getTicket(), String.valueOf(status)
                        , String.valueOf(page), ComConstant.PAGESIZE)))
                .compose(CommonWrap.wrap());
    }

    public Observable<WxpayEntity> getWxpay(String user_ip, String order_number) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getWxpay(NetHelper.createBaseArgumentsMap(KeyValueCreator.getWxpay(UserInfoManager.getUserId()
                        , UserInfoManager.getTicket(), ComConstant.PAY_WECHAT
                        , user_ip, order_number)))
                .compose(CommonWrap.wrap());
    }

    public Observable<AlipayEntity> getAlipay(String user_ip, String order_number) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getAlipay(NetHelper.createBaseArgumentsMap(KeyValueCreator.getWxpay(UserInfoManager.getUserId()
                        , UserInfoManager.getTicket(), ComConstant.PAY_ALIPAY
                        , user_ip, order_number)))
                .compose(CommonWrap.wrap());
    }
}
