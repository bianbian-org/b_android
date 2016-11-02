package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.p.activity.JSInteractionActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JSInteractionActivityModel extends BaseModel<JSInteractionActivityPresenter> {

    public JSInteractionActivityModel(JSInteractionActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getUrl() {
        return getExtras().getString(Constant.JS_PAGE_JUMP_URL, "");
    }

    public Observable<TrueEntity> deductionWhenCall(String store_id, String user_tel
            , String shop_service_id) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .deductionWhenCall(NetHelper.createBaseArguments(KeyValueCreator.deductionWhenCall(
                        UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                        , UserManager.INSTANCE.getTicket()
                        , store_id
                        , user_tel
                        , shop_service_id)))
                .compose(CommonWrap.wrap());
    }
}
