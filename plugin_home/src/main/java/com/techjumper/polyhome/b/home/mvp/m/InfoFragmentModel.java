package com.techjumper.polyhome.b.home.mvp.m;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.p.fragment.InfoFragmentPresenter;
import com.techjumper.polyhome.b.home.net.KeyValueCreator;
import com.techjumper.polyhome.b.home.net.NetHelper;
import com.techjumper.polyhome.b.home.net.ServiceAPI;

import rx.Observable;


/**
 * Created by kevin on 16/4/28.
 */
public class InfoFragmentModel extends BaseModel<InfoFragmentPresenter> {

    public InfoFragmentModel(InfoFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<WeatherEntity> getWeatherInfo() {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getWeatherInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.getWeatherInfo(UserInfoManager.getFamilyId())))
                .compose(CommonWrap.wrap());
    }

    public Observable<CalendarEntity> getCalendarInfo() {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getCalendarInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.empty()))
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
