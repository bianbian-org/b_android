package com.techjumper.polyhomeb.mvp.m;

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
}
