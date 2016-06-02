package com.techjumper.polyhome.b.property.mvp.m;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
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

    public Observable<TrueEntity> submitComplaint(int type, String content) {
        KeyValuePair complaintPair = KeyValueCreator.submitComplaint(ComConstant.defaultUserId, ComConstant.defaultTicket, String.valueOf(type), content);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitComplaint(argument)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> submitRepair(int family_id, int repair_type, int repair_device, String note) {
        KeyValuePair complaintPair = KeyValueCreator.submitRepair(ComConstant.defaultUserId, ComConstant.defaultTicket, String.valueOf(family_id), String.valueOf(repair_type), String.valueOf(repair_device), note);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitRepair(argument)
                .compose(CommonWrap.wrap());
    }
}
