package com.techjumper.polyhome.b.home.net;

import com.techjumper.commonres.entity.CalendarEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.WeatherEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface ServiceAPI {

    @GET("get_weather")
    Observable<WeatherEntity> getWeatherInfo(@QueryMap Map<String, String> map);

    @GET("get_calendar")
    Observable<CalendarEntity> getCalendarInfo(@QueryMap Map<String, String> map);

    @GET("messages")
    Observable<InfoEntity> getInfo(@QueryMap Map<String, String> map);

    @GET("notices/list")
    Observable<NoticeEntity> getNotices(@QueryMap Map<String, String> map);
}
