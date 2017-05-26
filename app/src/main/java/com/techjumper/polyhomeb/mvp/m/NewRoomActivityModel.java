package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.NewRoomEntity;
import com.techjumper.polyhomeb.mvp.p.activity.NewRoomActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class NewRoomActivityModel extends BaseModel<NewRoomActivityPresenter> {

    public NewRoomActivityModel(NewRoomActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<NewRoomEntity> newRoom(String room_name) {
        KeyValuePair keyValuePair = KeyValueCreator.newRoom(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , room_name
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID));
        BaseArgumentsEntity baseArguments = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .newRoom(baseArguments)
                .compose(CommonWrap.wrap());
    }
}
