package com.techjumper.polyhome.b.setting.net;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.LoginEntity;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kevin on 16/5/3.
 */
public interface ServiceAPI {

    @POST("login")
    Observable<LoginEntity> login(@Body BaseArgumentsEntity entity);

    @POST("user/update_password")
    Observable<LoginEntity> changePassword(@Body BaseArgumentsEntity entity);

    @POST("user/profile")
    Observable<LoginEntity> updateUserInfo(@Body BaseArgumentsEntity entity);
}
