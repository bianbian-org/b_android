package com.techjumper.polyhomeb.net;


import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface ServiceAPI {

    /**
     * 发送验证码
     * <p>
     * params:
     * mobile # 手机号码, *表示必填, 下同。
     * type # 短信类型：1(缺省)-注册，2-找回密码。
     * return:
     * result: true # 这里表示成功返回状态时data的内容。
     * <p>
     * # ERROR CODE
     * error_code: 101,	error_msg: '手机号码不正确！'
     * error_code: 102,	error_msg: '该手机号码已经注册！'
     * error_code: 103,	error_msg: '该手机号码没有注册！'
     * error_code: 104,	error_msg: '操作太频繁，请稍后再试！'
     */
    @GET("send_captcha")
    Observable<TrueEntity> sendVerificationCode(@QueryMap Map<String, String> args);

    /**
     * 注册
     * <p>
     * params:
     * mobile # 手机号码
     * sms_captcha # 手机码验证码
     * password # 密码
     * return:
     * id: 1
     * mobile: "18030405923"
     * ticket: "5190bce3e93635aa4d0cb45f664d59936bcbfb1a" #登录过期票据，有效期5天
     * <p>
     * # ERROR CODE
     * error_code: 101,	error_msg: '手机号码不正确！'
     * error_code: 106,	error_msg: '密码不能少于8位！'
     * error_code: 107,	error_msg: '验证码错误！'post '/register'
     */
    @POST("register")
    Observable<LoginEntity> regist(@Body BaseArgumentsEntity entity);

    /**
     * 登陆
     * <p>
     * params:
     * mobile # 手机号码
     * password # 密码
     * return:
     * 同注册接口
     * <p>
     * # ERROR CODE
     * error_code: 103,	error_msg: '该手机号码没有注册'
     * error_code: 108,	error_msg: '用户名或密码错误！'
     */
    @POST("login")
    Observable<LoginEntity> login(@Body BaseArgumentsEntity entity);

}
