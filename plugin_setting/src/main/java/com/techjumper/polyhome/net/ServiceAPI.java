package com.techjumper.polyhome.net;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.LoginEntity;

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

    @POST("login")
    Observable<LoginEntity> login(@Body BaseArgumentsEntity entity);

}
