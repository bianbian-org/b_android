package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.RenameRoomEntity;
import com.techjumper.polyhomeb.mvp.p.activity.RenameRoomActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Observable;

import static com.techjumper.polyhomeb.mvp.p.activity.RoomManageActivityPresenter.KEY_ROOM_ID;
import static com.techjumper.polyhomeb.mvp.p.activity.RoomManageActivityPresenter.KEY_ROOM_NAME;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class RenameRoomActivityModel extends BaseModel<RenameRoomActivityPresenter> {

    public RenameRoomActivityModel(RenameRoomActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<RenameRoomEntity> renameRoom(String room_name) {
        KeyValuePair keyValuePair = KeyValueCreator.renameRoom(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getRoomId()
                , room_name);
        BaseArgumentsEntity baseArguments = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .renameRoom(baseArguments)
                .compose(CommonWrap.wrap());
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getRoomName() {
        return getExtras().getString(KEY_ROOM_NAME, "");
    }

    private String getRoomId() {
        return getExtras().getString(KEY_ROOM_ID, "");
    }

}
