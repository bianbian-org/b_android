package com.techjumper.polyhome.b.property.net;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.InfoEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kevin on 16/5/3.
 */
public interface ServiceAPI {

    @GET("notices")
    Observable<AnnouncementEntity> getAnnouncements(@QueryMap Map<String, String> map);
}
