package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.mvp.p.activity.LoginActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class LoginActivityModel extends BaseModel<LoginActivityPresenter> {

    public LoginActivityModel(LoginActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<LoginEntity> login(String mobile, String password) {
        KeyValuePair loginPair = KeyValueCreator.login(mobile, password);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(loginPair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .login(argument)
                .compose(CommonWrap.wrap());
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getComeFrom() {
        if (getExtra() != null) {  //如果不加判断的话,正常登陆会报空。因为在SplashActivity过来的时候没有带Bundle.所以如果这里不想判断的话,在SplashActivity跳转的时候带个Bundle,里面是空的也行
            return getExtra().getString(getPresenter().KEY_COME_FROM, "");
        } else {
            return "";
        }
    }
}
