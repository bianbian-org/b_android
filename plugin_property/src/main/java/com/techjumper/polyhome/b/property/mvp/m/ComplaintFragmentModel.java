package com.techjumper.polyhome.b.property.mvp.m;

import android.util.Log;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.util.StringUtil;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ComplaintFragmentPresenter;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ActionFragment;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/11/2.
 */

public class ComplaintFragmentModel extends BaseModel<ComplaintFragmentPresenter> {

    public ComplaintFragmentModel(ComplaintFragmentPresenter presenter) {
        super(presenter);
    }

    /**
     * 获取建议列表
     * @param page
     * @return
     */
    public Observable<ComplaintEntity> getComplaints(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getComplaints(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaints(UserInfoManager.getUserId(), UserInfoManager.getTicket(), String.valueOf(page), ComConstant.PAGESIZE)))
                .compose(CommonWrap.wrap());
    }

    /**
     * 获取建议详情
     * @param id
     * @return
     */
    public Observable<ComplaintDetailEntity> getComplaintDetail(long id) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getComplaintDetail(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaintDetail(UserInfoManager.getUserId(), UserInfoManager.getTicket(), String.valueOf(id))))
                .compose(CommonWrap.wrap());
    }

    /**
     * 回复
     * @param id
     * @param content
     * @return
     */
    public Observable<TrueEntity> replyMessage(long id, String content) {
        KeyValuePair complaintPair = KeyValueCreator.replyComplaint(UserInfoManager.getUserId(), UserInfoManager.getTicket(), content, String.valueOf(id));
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .replyComplaint(argument)
                .compose(CommonWrap.wrap());
    }

    /**
     * 提交一次建议
     * @param type
     * @param content
     * @param mobile
     * @return
     */
    public Observable<TrueEntity> submitComplaint(int type, String content, String mobile) {
        KeyValuePair complaintPair = KeyValueCreator.submitComplaint(UserInfoManager.getUserId(), UserInfoManager.getTicket(), String.valueOf(type), content, mobile);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitComplaint(argument)
                .compose(CommonWrap.wrap());
    }

    /**
     * 一次心跳
     * @return
     */
    public Observable<HeartbeatEntity> submitOnline() {
        KeyValuePair keyValuePair = KeyValueCreator.submitOnline(UserInfoManager.getTicket(), StringUtil.getMacAddress());
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        Log.d("submitOnline", "familyId: " + UserInfoManager.getFamilyId() + "  deviceId: " + StringUtil.getMacAddress());
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitOnline(argument)
                .compose(CommonWrap.wrap());
    }
}
