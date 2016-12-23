package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.VillageLockEntity;
import com.techjumper.polyhomeb.mvp.p.activity.JoinVillageActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JoinVillageActivityModel extends BaseModel<JoinVillageActivityPresenter> {

    public JoinVillageActivityModel(JoinVillageActivityPresenter presenter) {
        super(presenter);
    }

    public Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getName() {
        return getExtra().getString(Constant.CHOOSE_VILLAGE_NAME, "");
    }

    public int getId() {
        return getExtra().getInt(Constant.CHOOSE_VILLAGE_ID, -1);
    }

    public Observable<TrueEntity> joinVillage(String building, String unit, String room) {
        KeyValuePair keyValuePair = KeyValueCreator.joinVillage(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getId()
                , building
                , unit
                , room);
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .joinVillage(entity)
                .compose(CommonWrap.wrap());
    }

    public Observable<BluetoothLockDoorInfoEntity> getBLEDoorInfo() {
        KeyValuePair keyValuePair = KeyValueCreator.getBLEDoorInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getBLEDoorInfo(map)
                .compose(CommonWrap.wrap());
    }

    public Observable<VillageLockEntity> getVillageLocks() {
        KeyValuePair keyValuePair = KeyValueCreator.getVillageLocks(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getVillageLocks(map)
                .compose(CommonWrap.wrap());
    }
}
