package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.JoinFamilyEntity;
import com.techjumper.polyhomeb.mvp.p.activity.ScanHostQRCodeActivityPresenter;
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
public class ScanHostQRCodeActivityModel extends BaseModel<ScanHostQRCodeActivityPresenter> {

    public ScanHostQRCodeActivityModel(ScanHostQRCodeActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<JoinFamilyEntity> joinFamily(String result) {
        KeyValuePair keyValuePair = KeyValueCreator.joinFamily(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , result);
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .joinFamily(entity)
                .compose(CommonWrap.wrap());
    }

    public Observable<BluetoothLockDoorInfoEntity> getBLEDoorInfo(String village_id) {
        KeyValuePair keyValuePair = KeyValueCreator.getBLEDoorInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , village_id);
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getBLEDoorInfo(map)
                .compose(CommonWrap.wrap());
    }
}
