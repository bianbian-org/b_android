package com.techjumper.polyhome.net;

import com.techjumper.commonres.entity.InfoEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kevin on 16/5/3.
 */
public interface ServiceAPI {

    @GET("messages")
    Observable<InfoEntity> getInfo(@QueryMap Map<String, String> map);
    
}
