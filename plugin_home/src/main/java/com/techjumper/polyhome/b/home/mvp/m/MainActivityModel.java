package com.techjumper.polyhome.b.home.mvp.m;

import android.util.Log;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.util.StringUtil;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.p.activity.MainActivityPresenter;
import com.techjumper.polyhome.b.home.net.KeyValueCreator;
import com.techjumper.polyhome.b.home.net.NetHelper;
import com.techjumper.polyhome.b.home.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/7/29.
 */

public class MainActivityModel extends BaseModel<MainActivityPresenter> {

    public MainActivityModel(MainActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<HeartbeatEntity> submitOnline() {
        KeyValuePair keyValuePair = KeyValueCreator.submitOnline(UserInfoManager.getFamilyId(), StringUtil.getMacAddress());
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        Log.d("submitOnline", "familyId: " + UserInfoManager.getFamilyId() + "  deviceId: " + StringUtil.getMacAddress());
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitOnline(argument)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> submitClicks(String clicks) {
        KeyValuePair keyValuePair = KeyValueCreator.submitClicks(clicks);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitClicks(argument)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> submitTimer(String timer) {
        KeyValuePair keyValuePair = KeyValueCreator.submitTimer(UserInfoManager.getFamilyId(), timer);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitTimer(argument)
                .compose(CommonWrap.wrap());
    }
}
