package com.techjumper.polyhome.b.property.mvp.m;

import android.util.Log;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.util.StringUtil;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ActionFragmentPresenter;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/5/17.
 */
public class ActionFragmentModel extends BaseModel<ActionFragmentPresenter> {

    public ActionFragmentModel(ActionFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<TrueEntity> submitComplaint(int type, String content, String mobile) {
        KeyValuePair complaintPair = KeyValueCreator.submitComplaint(UserInfoManager.getUserId(), UserInfoManager.getTicket(), String.valueOf(type), content, mobile);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitComplaint(argument)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> submitRepair(int repair_type, int repair_device, String note, String mobile) {
        KeyValuePair complaintPair = KeyValueCreator.submitRepair(UserInfoManager.getUserId(), UserInfoManager.getTicket(), UserInfoManager.getFamilyId(), String.valueOf(repair_type), String.valueOf(repair_device), note, mobile);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitRepair(argument)
                .compose(CommonWrap.wrap());
    }

    public Observable<HeartbeatEntity> submitOnline() {
        KeyValuePair keyValuePair = KeyValueCreator.submitOnline(UserInfoManager.getTicket(), StringUtil.getMacAddress());
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        Log.d("submitOnline", "familyId: " + UserInfoManager.getFamilyId() + "  deviceId: " + StringUtil.getMacAddress());
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitOnline(argument)
                .compose(CommonWrap.wrap());
    }
}
