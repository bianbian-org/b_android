package com.techjumper.polyhome_b.adlib.net;

import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface ServiceAPI {

    /**
     * pad广告
     * <p>
     * parms:
     * family_id: 442,
     * user_id: 1,
     * ticket:
     * return:
     * {
     * rules:{
     * 1001: # 广告位
     * {
     * 0:{id:111,time_length:300},
     * 1:{id:111,time_length:300},
     * ...
     * }
     * },
     * ads:{
     * 111:
     * [
     * {
     * media_type: 1, # 类型，1-图片，2-视频
     * media_url: "http://pl.techjumper.com/upload/files/about_drop.jpg",
     * md5: '8be36e9910c825ae0cfd30d1b5863949'
     * running_time: "5", # 执行时间，单位秒
     * url: "#" # 跳转地址
     * }
     * ]
     * }
     * }
     * 测试地址：http://pl.techjumper.com/api/v1b/ad/pad?sign=6586f02df8545b325892e8a0bfe5037b&data=%7B%22family_id%22%3A442%7D
     */
    @GET("ad/pad")
    Observable<AdEntity> padAd(@QueryMap Map<String, String> args);

    /**
     * 下载文件
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
