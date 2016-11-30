package com.techjumper.polyhome.b.property.mvp.m;

import android.util.Log;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.util.StringUtil;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.fragment.RepairFragmentPresenter;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ActionFragment;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/10/28.
 */

public class RepairFragmentModel extends BaseModel<RepairFragmentPresenter> {

    public RepairFragmentModel(RepairFragmentPresenter presenter) {
        super(presenter);
    }

    /**
     * 获取保修列表
     *
     * @param page
     * @return
     */
    public Observable<RepairEntity> getRepairs(int page) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getRepair(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaints(UserInfoManager.getUserId(), UserInfoManager.getTicket(), String.valueOf(page), ComConstant.PAGESIZE)))
                .compose(CommonWrap.wrap());
    }

    /**
     * 提交保修对话信息
     *
     * @param id
     * @param content
     * @return
     */
    public Observable<TrueEntity> replyMessage(long id, String content) {
        KeyValuePair repairPair = KeyValueCreator.replyRepair(UserInfoManager.getUserId(), UserInfoManager.getTicket(), content, String.valueOf(id));
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(repairPair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .replyRepair(argument)
                .compose(CommonWrap.wrap());
    }

    /**
     * 获取保修详情
     *
     * @param id
     * @return
     */
    public Observable<RepairDetailEntity> getRepairDetail(long id) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getRepairDetail(NetHelper.createBaseArgumentsMap(KeyValueCreator.getRepairDetail(UserInfoManager.getUserId(), UserInfoManager.getTicket(), String.valueOf(id))))
                .compose(CommonWrap.wrap());
    }

    /**
     * 提交报修
     * @param repair_type
     * @param repair_device
     * @param note
     * @param mobile
     * @return
     */
    public Observable<TrueEntity> submitRepair(int repair_type, int repair_device, String note, String mobile) {
        KeyValuePair complaintPair = KeyValueCreator.submitRepair(UserInfoManager.getUserId(), UserInfoManager.getTicket(), UserInfoManager.getFamilyId(), String.valueOf(repair_type), String.valueOf(repair_device), note, mobile);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitRepair(argument)
                .compose(CommonWrap.wrap());
    }

    /**
     * 提交一次心跳
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
