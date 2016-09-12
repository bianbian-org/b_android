package com.techjumper.polyhome.b.property.net;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
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

    @GET("notices")
    Observable<AnnouncementEntity> getAnnouncements(@QueryMap Map<String, String> map);

    @POST("suggestions")
    Observable<TrueEntity> submitComplaint(@Body BaseArgumentsEntity entity);

    @GET("suggestions")
    Observable<ComplaintEntity> getComplaints(@QueryMap Map<String, String> map);

    @POST("repairs")
    Observable<TrueEntity> submitRepair(@Body BaseArgumentsEntity entity);

    @GET("repairs")
    Observable<RepairEntity> getRepair(@QueryMap Map<String, String> map);

    @GET("suggestions/show")
    Observable<ComplaintDetailEntity> getComplaintDetail(@QueryMap Map<String, String> map);

    @POST("suggestions/reply")
    Observable<TrueEntity> replyComplaint(@Body BaseArgumentsEntity entity);

    @GET("repairs/show")
    Observable<RepairDetailEntity> getRepairDetail(@QueryMap Map<String, String> map);

    @POST("repairs/reply")
    Observable<TrueEntity> replyRepair(@Body BaseArgumentsEntity entity);

    @POST("family/online")
    Observable<HeartbeatEntity> submitOnline(@Body BaseArgumentsEntity entity);
}
