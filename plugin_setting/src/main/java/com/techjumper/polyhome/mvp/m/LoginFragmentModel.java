package com.techjumper.polyhome.mvp.m;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.LoginEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.mvp.p.fragment.LoginFragmentPresenter;
import com.techjumper.polyhome.net.KeyValueCreator;
import com.techjumper.polyhome.net.NetHelper;
import com.techjumper.polyhome.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/5/9.
 */
public class LoginFragmentModel extends BaseModel<LoginFragmentPresenter> {

    public LoginFragmentModel(LoginFragmentPresenter presenter) {
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
