package com.techjumper.polyhome.b.property.mvp.m;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.activity.MainActivityPresenter;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/10/9.
 */

public class MainActivityModel extends BaseModel<MainActivityPresenter>{

    public MainActivityModel(MainActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<TrueEntity> submitTimer(String timer) {
        KeyValuePair keyValuePair = KeyValueCreator.submitTimer(UserInfoManager.getFamilyId(), timer);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitTimer(argument)
                .compose(CommonWrap.wrap());
    }
}
