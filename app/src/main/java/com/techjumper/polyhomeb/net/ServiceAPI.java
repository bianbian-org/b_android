package com.techjumper.polyhomeb.net;


import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.entity.AvatarEntity;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.C_AllMemberEntity;
import com.techjumper.polyhomeb.entity.C_AllRoomEntity;
import com.techjumper.polyhomeb.entity.C_RoomsByMemberEntity;
import com.techjumper.polyhomeb.entity.CheckInEntity;
import com.techjumper.polyhomeb.entity.JoinFamilyEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.MarqueeTextInfoEntity;
import com.techjumper.polyhomeb.entity.MessageEntity;
import com.techjumper.polyhomeb.entity.NewRoomEntity;
import com.techjumper.polyhomeb.entity.OrdersEntity;
import com.techjumper.polyhomeb.entity.PaymentTypeEntity;
import com.techjumper.polyhomeb.entity.PaymentsEntity;
import com.techjumper.polyhomeb.entity.PropertyComplainDetailEntity;
import com.techjumper.polyhomeb.entity.PropertyComplainEntity;
import com.techjumper.polyhomeb.entity.PropertyPlacardEntity;
import com.techjumper.polyhomeb.entity.PropertyRepairDetailEntity;
import com.techjumper.polyhomeb.entity.PropertyRepairEntity;
import com.techjumper.polyhomeb.entity.QueryFamilyEntity;
import com.techjumper.polyhomeb.entity.RenameRoomEntity;
import com.techjumper.polyhomeb.entity.SectionsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UpdateInfoEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.entity.VillageEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.BaseArgumentsMedicalEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalChangeAccountEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalMainEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalVerificationCodeEntity;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    /**
     * 找回密码
     * post '/password/reset'
     * params:
     * mobile # 手机号码
     * sms_captcha # 验证码
     * new_password # 新密码
     * return:
     * result: true
     * # ERROR CODE
     * error_code: 101,	error_msg: '手机号码不正确！'
     * error_code: 103,	error_msg: '该手机号码没有注册'
     * error_code: 106,	error_msg: '密码不能少于8位！'
     * error_code: 107,	error_msg: '验证码错误！'
     */
    @POST("password/reset")
    Observable<TrueEntity> findPassword(@Body BaseArgumentsEntity entity);

    /**
     * 物业管理-物业公告列表
     * <p>
     * get '/notices'
     * <p>
     * params:
     * user_id # 用户ID
     * family_id:1,
     * ticket # session登录验证
     * page: 1 #页号
     * count:3 #每页数据条数
     * "data": {
     * "notices": [
     * {
     * "id": 10,    #公告ID
     * "title": "54trerf",   #公告标题
     * "types": 1,  #公告类型 1-公告 2-资讯
     * "content": "u003cpu003e3243erewu003c/pu003e", #内容
     * "time": "2016-05-06"  #时间
     * },
     * {
     * "id": 9,
     * "title": "tgrs345",
     * "types": 1,
     * "content": "u003cpu003ef34545tu003c/pu003e",
     * "time": "2016-05-06"
     * },
     */
    @GET("notices")
    Observable<PropertyPlacardEntity> propertyNotice(@QueryMap Map<String, String> args);

//    /**
//     * 公告详情
//     * get '/notices/show'
//     * params:
//     * user_id # 用户ID
//     * ticket # session登录验证
//     * id #公告ID
//     * return:
//     * "error_code": 0,
//     * "error_msg": null,
//     * "data": {
//     * "id": 10,  #公告ID
//     * "title": "54trerf", #公告标题
//     * "types": 1,  #公告类型 1-公告 2-资讯
//     * "content": "u003cpu003e3243erewu003c/pu003e",  #内容
//     * "time": "2016-05-06"  #时间
//     */
//    @GET("notices/show")
//    Observable<PropertyPlacardDetailEntity> propertyNoticeDetail(@QueryMap Map<String, String> stringStringMap);

    /**
     * 物业管理-投诉建议列表
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * page: 1 #页号
     * count:3 #每页数据条数
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @GET("suggestions")
    Observable<PropertyComplainEntity> propertyComplain(@QueryMap Map<String, String> stringStringMap);

    /**
     * get '/repairs'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * status:0 #报修状态 0-未处理 1-已回复 2-已处理 3-已关闭(查询全部则为空)
     * page: 1 #页号
     * count:3 #每页数据条数
     * <p>
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @GET("repairs")
    Observable<PropertyRepairEntity> propertyRepair(@QueryMap Map<String, String> stringStringMap);

    /**
     * 新建投诉
     * post '/suggestions'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * mobile #用户联系电话
     * types #投诉类型 1-投诉 2-建议 3-表扬
     * content #内容
     * imgs: "['img1.jpg','img2.jpg','img3.jpg']"  #多个图片信息（数组）
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @POST("suggestions")
    Observable<TrueEntity> newComplain(@Body BaseArgumentsEntity entity);

    /**
     * 新建报修
     * post '/repairs'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * family_id #家庭ID
     * mobile #用户手机号
     * repair_type #报修类型 1-个人报修 2-公共区域报修
     * repair_device #报修设备 1-门窗类 2-水电类 3-锁类 4-电梯类 5-墙类
     * note #备注
     * imgs: ['img1.jpg','img2.jpg','img3.jpg']  #多个图片信息（数组）
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     */
    @POST("repairs")
    Observable<TrueEntity> newRepair(@Body BaseArgumentsEntity entity);

    /**
     * 上传图片
     * post '/upload/image/base64'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * file # 上传文件base64编码
     * return:
     * url: # 上传成功返回url
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 208,	error_msg: '上传文件失败'
     */
    @POST("upload/image/base64")
    Observable<UploadPicEntity> uploadPic(@Body BaseArgumentsEntity entity);

    /**
     * 投诉详情(聊天)
     * get '/suggestions/show'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * id #投诉消息ID
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @GET("suggestions/show")
    Observable<PropertyComplainDetailEntity> getComplainDetail(@QueryMap Map<String, String> stringStringMap);

    /**
     * 物业管理-投诉建议回复
     * post '/suggestions/reply'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * content # 回复内容
     * suggestion_id #投诉消息ID
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @POST("suggestions/reply")
    Observable<TrueEntity> complainDetailReply(@Body BaseArgumentsEntity entity);


    /**
     * 报修详情
     * get '/repairs/show'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * repair_id #报修记录ID
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "id": 1
     * "repair_type": 1,
     * "repair_device": 3,
     * "status": 0,
     * "repair_date": "2016-05-16",
     * "note": "备注",
     * "imgs": [      #图片信息
     * '/upload/images/img1.jpg',
     * '/upload/images/img2.jpg',
     * '/upload/images/img3.jpg'
     * ],
     * "replies": [
     * {
     * "user_id": 1,  #用户ID
     * "content": "啦啦啦已提交报修", #回复内容
     * "time": "2016-05-1617: 37" #回复时间
     * },
     * {
     * "user_id": 1,
     * "content": "这里是回复",
     * "time": "2016-05-1617: 44"
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @GET("repairs/show")
    Observable<PropertyRepairDetailEntity> getRepairDetail(@QueryMap Map<String, String> stringStringMap);


    /**
     * 报修-回复
     * post '/repairs/reply'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * content # 回复内容
     * repair_id #报修记录ID
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     * <p>
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 404,	error_msg: '未找到内容！'
     */
    @POST("repairs/reply")
    Observable<TrueEntity> repairDetailReply(@Body BaseArgumentsEntity entity);

    /**
     * 回复帖子
     */
    @POST("comment")
    Observable<TrueEntity> replyComment(@Body BaseArgumentsEntity entity);

    /**
     * 友邻-获取版块接口
     */
    @GET("sections")
    Observable<SectionsEntity> getSections(@QueryMap Map<String, String> stringStringMap);

    /**
     * 友邻发帖或者闲置
     */
    @POST("article")
    Observable<TrueEntity> newArticle(@Body BaseArgumentsEntity entity);

    /**
     * 省份小区列表
     * get '/village'
     * user_id # 用户ID
     * ticket # session登录验证
     */
    @GET("village")
    Observable<VillageEntity> getVillages(@QueryMap Map<String, String> stringStringMap);

    /**
     * 加入小区
     * post '/village_users/join'
     * <p>
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * village_id # 小区ID
     * building:1  # 楼栋号
     * unit:1  # 单元号
     * index:1  # 房间号
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true"
     * }
     * }
     * <p>
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     * error_code: 311,	error_msg: '小区不存在！'
     * error_code: 312,	error_msg: '用户已加入该小区或已提交审核！'
     */
    @POST("village_users/join")
    Observable<TrueEntity> joinVillage(@Body BaseArgumentsEntity entity);

    /**
     * 查询用户所在家庭和小区
     * get '/user/families_villages'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     */
    @GET("user/families_villages")
    Observable<UserFamiliesAndVillagesEntity> getFamilyAndVillage(@QueryMap Map<String, String> stringStringMap);

    /**
     * 获取消息
     * *user_id #
     * ticket #
     * type: 1-系统信息 2-订单信息 4-物业信息 6-友邻(参数可选，没有该参数则为全部查询)
     * page: 1  #当前页数
     * count: 5   #每页数据条数
     */
    @GET("messages")
    Observable<MessageEntity> getMessages(@QueryMap Map<String, String> stringStringMap);

    /**
     * 更改用户信息
     * post '/user/profile'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * username # 用户昵称
     * sex # 1-男 2-女
     * birthday #生日 2010-12-21
     * email #邮箱
     * return:
     * 同登录接口
     * <p>
     * # ERROR CODE
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @POST("user/profile")
    Observable<LoginEntity> setUserInfo(@Body BaseArgumentsEntity entity);

    /**
     * 修改用户头像
     * post '/user/update_cover'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * cover # 图像上传接口返回的图片地址
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "cover": "/upload/images/11f0d2a32b3085b05160a2943d1aec13.jpg"
     * }
     * }
     */
    @POST("user/update_cover")
    Observable<AvatarEntity> updateAvatar(@Body BaseArgumentsEntity entity);

    /**
     * 获取订单数据
     * get '/estate_orders'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * family_id #家庭ID
     * status #缴费状态 1-未缴费 2-已缴费
     * pay_type #缴费类型 1-物业费 2-水费 3-电费 4-燃气费 5-其他（查询所有此字段为空）
     * page: 1  #当前页数
     * count: 5   #每页数据条数
     */
    @GET("estate_orders")
    Observable<OrdersEntity> getOrdersInfo(@QueryMap Map<String, String> map);

    /**
     * 用户签到
     * <p>
     * post '/sign'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "result": "true",
     * "sign_days": [8,9]      #本月签到日
     * }
     * }
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @POST("sign")
    Observable<CheckInEntity> checkIn(@Body BaseArgumentsEntity entity);

    /**
     * 获取签到数据
     * <p>
     * post '/sign'
     * params:
     * user_id # 用户ID
     * ticket # session登录验证
     * return:
     * {
     * "error_code": 0,
     * "error_msg": null,
     * "data": {
     * "sign_days": [8,9]      #本月签到日
     * }
     * }
     * error_code: 109,	error_msg: '此功能登录后可使用！'
     */
    @GET("sign")
    Observable<CheckInEntity> getCheckInData(@QueryMap Map<String, String> stringStringMap);

    /**
     * 获取蓝牙门锁信息
     */
    @GET("outdoor_pads")
    Observable<BluetoothLockDoorInfoEntity> getBLEDoorInfo(@QueryMap Map<String, String> stringStringMap);

    /**
     * 发起支付请求
     */
    @GET("payments")
    Observable<PaymentsEntity> payments(@QueryMap Map<String, String> q);

    /**
     * 查询家庭信息
     */
    @POST("family_query")
    Observable<QueryFamilyEntity> queryFamilyInfo(@Body BaseArgumentsEntity entity);

    /**
     * 检查更新
     */
    @GET("update/apk")
    Observable<UpdateInfoEntity> getAppUpdateInfo(@QueryMap Map<String, String> map);

    /**
     * 下载新版本apk
     */
    @GET
    @Streaming
    Observable<ResponseBody> downloadNewApk(@Url String url);

    /**
     * 生活服务-联系商家扣费接口
     */
    @POST("store/charge")
    Observable<TrueEntity> deductionWhenCall(@Body BaseArgumentsEntity entity);

    /**
     * 友邻删除自己的帖子
     */
    @POST("article/delete")
    Observable<TrueEntity> deleteArticle(@Body BaseArgumentsEntity entity);

    /**
     * 扫描二维码加入家庭
     */
    @POST("family/join")
    Observable<JoinFamilyEntity> joinFamily(@Body BaseArgumentsEntity entity);

    /**
     * 上传图片(文件方式)
     */
    @POST("upload/images")
    Observable<UploadPicEntity> uploadPicFile(@Body MultipartBody body);

    /**
     * 获取物业缴费类型
     */
    @GET("estate_orders/estate_items")
    Observable<PaymentTypeEntity> getPaymentType(@QueryMap Map<String, String> map);

    /**
     * 转交管理员权限
     */
    @POST("family/change_manager")
    Observable<TrueEntity> transferAuthority(@Body BaseArgumentsEntity entity);

    /**
     * 获取首页滚动文字信息列表
     */
    @GET("messages/notices")
    Observable<MarqueeTextInfoEntity> getMarqueeText(@QueryMap Map<String, String> map);

    /**
     * 消息已读未读
     */
    @POST("message_read")
    Observable<TrueEntity> updateMessageState(@Body BaseArgumentsEntity entity);


    /**
     * 首页广告
     */
    @GET("advertisements")
    Observable<ADEntity> getADInfo(@QueryMap Map<String, String> map);


/*********************************************************医疗接口*********************************************************/

    /**
     * 医疗登录接口,devicetype=1代表安卓,logintype=1代表用户名登录
     */
    @POST("nruaservice/user/login?devicetype=1&logintype=1")
    Observable<Result<MedicalUserLoginEntity>> medicalUserLogin(@Header("Authorization") String value
            , @Body BaseArgumentsMedicalEntity entity);

    /**
     * 医疗修改用户信息接口
     */
    @POST("nruaservice/user/updateuser")
    Observable<Result<MedicalStatusEntity>> changeUserInfo(@Header("nztoken") String token, @QueryMap Map<String, String> params);

    /**
     * 医疗修改用户性别接口
     */
    @POST("nruaservice/user/updateuser")
    Observable<Result<MedicalStatusEntity>> changeUserSexInfo(@Header("nztoken") String token, @QueryMap Map<String, Integer> params);

    /**
     * 医疗修改用户邮箱接口
     */
    @POST("nruaservice/user/updateuser")
    Observable<Result<MedicalStatusEntity>> changeUserEmailInfo(@Header("Cookie") String cookie, @Header("nztoken") String token, @QueryMap Map<String, String> params);

    /**
     * 医疗下发验证码接口
     */
    @GET("nruaservice/user/getcode")
    Observable<Result<MedicalVerificationCodeEntity>> getVerificationCode(@Header("nztoken") String token, @QueryMap Map<String, String> params);

    /**
     * 医疗请求健康数据
     */
    @GET("bizservice2/rest/health/data/{type}")
    Observable<Result<MedicalMainEntity>> getMedicalMainData(@Header("nztoken") String token
            , @Path("type") String measure_type
            , @QueryMap Map<String, String> stringMap);

    @POST("signs")
    Observable<MedicalChangeAccountEntity> getMedicalUserData(@Body BaseArgumentsEntity entity);


    /**********************************C端接口*************************************/

    /**
     * 查询所有房间
     */
    @POST("room_user_queryrooms")
    Observable<C_AllRoomEntity> getAllRooms(@Body BaseArgumentsEntity entity);

    /**
     * 删除某房间
     */
    @POST("room_delete")
    Observable<TrueEntity> deleteRoom(@Body BaseArgumentsEntity entity);

    /**
     * 新建房间
     */
    @POST("room")
    Observable<NewRoomEntity> newRoom(@Body BaseArgumentsEntity entity);

    /**
     * 重命名房间
     */
    @POST("room_update")
    Observable<RenameRoomEntity> renameRoom(@Body BaseArgumentsEntity entity);

    /**
     * 查询所有成员
     */
    @POST("family_user_queryusers")
    Observable<C_AllMemberEntity> getAllMember(@Body BaseArgumentsEntity entity);

    /**
     * 通过成员id和家庭id查询此成员在此家庭下的房间
     */
    @POST("room_user_queryrooms")
    Observable<C_RoomsByMemberEntity> getRoomsByMember(@Body BaseArgumentsEntity entity);

    /**
     * 删除家庭内某个成员
     */
    @POST("family_user_delete")
    Observable<TrueEntity> deleteMember(@Body BaseArgumentsEntity entity);

    /**
     * 删除房间用户
     */
    @POST("room_user_delete")
    Observable<TrueEntity> deleteMemberFromRoom(@Body BaseArgumentsEntity entity);

    /**
     * 新增房间用户
     */
    @POST("room_user_add")
    Observable<TrueEntity> addMemberToRoom(@Body BaseArgumentsEntity entity);


}

