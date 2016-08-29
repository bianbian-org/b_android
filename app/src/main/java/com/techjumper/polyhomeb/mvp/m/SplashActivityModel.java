package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.mvp.p.activity.SplashActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SplashActivityModel extends BaseModel<SplashActivityPresenter> {

    public SplashActivityModel(SplashActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<UserFamiliesAndVillagesEntity> getFamilyAndVillage() {
        KeyValuePair keyValuePair = KeyValueCreator.getFamilyAndVillage(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket());
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getFamilyAndVillage(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }
}
