package com.techjumper.polyhome.mvp.m;

import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.mvp.p.fragment.InfoFragmentPresenter;
import com.techjumper.polyhome.net.KeyValueCreator;
import com.techjumper.polyhome.net.NetHelper;
import com.techjumper.polyhome.net.ServiceAPI;

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
                .getWeatherInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.empty()))
                .compose(CommonWrap.wrap());
    }

    public Observable<CalendarEntity> getCalendarInfo() {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getCalendarInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.empty()))
                .compose(CommonWrap.wrap());
    }
}
