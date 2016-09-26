package com.techjumper.polyhome.b.home.mvp.m;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.p.activity.AdNewActivityPresenter;
import com.techjumper.polyhome.b.home.net.KeyValueCreator;
import com.techjumper.polyhome.b.home.net.NetHelper;
import com.techjumper.polyhome.b.home.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/9/26.
 */

public class AdNewActivityModel extends BaseModel<AdNewActivityPresenter> {

    public AdNewActivityModel(AdNewActivityPresenter presenter) {
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
