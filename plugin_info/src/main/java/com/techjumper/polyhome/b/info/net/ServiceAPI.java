package com.techjumper.polyhome.b.info.net;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.TrueEntity;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kevin on 16/5/3.
 */
public interface ServiceAPI {

    @GET("messages")
    Observable<InfoEntity> getInfo(@QueryMap Map<String, String> map);

    @POST("message_read")
    Observable<TrueEntity> readMessage(@Body BaseArgumentsEntity entity);

    @GET("notices")
    Observable<AnnouncementEntity> getAnnouncements(@QueryMap Map<String, String> map);

    @POST("family/online")
    Observable<HeartbeatEntity> submitOnline(@Body BaseArgumentsEntity entity);

    @POST("module_statistics")
    Observable<TrueEntity> submitTimer(@Body BaseArgumentsEntity entity);
}
