package com.techjumper.polyhomeb.net;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.QueryFamilyEntity;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Observable;

public class NetExecutor {

    private NetExecutor() {
    }

    private static class SingletonInstance {
        private static final NetExecutor INSTANCE = new NetExecutor();
    }

    public static NetExecutor getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public Observable<QueryFamilyEntity> queryFamilyInfo() {
        String familyId = UserManager.INSTANCE.getCurrentFamilyInfo(UserManager.KEY_CURRENT_FAMILY_ID);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .queryFamilyInfo(NetHelper.createBaseArguments(KeyValueCreator.queryFamily(
                        UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                        , UserManager.INSTANCE.getTicket()
                        , familyId
                )))
                .compose(CommonWrap.wrap());
    }
}