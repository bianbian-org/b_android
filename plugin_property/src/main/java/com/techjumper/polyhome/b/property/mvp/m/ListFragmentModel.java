package com.techjumper.polyhome.b.property.mvp.m;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ListFragmentPresenter;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ActionFragment;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/5/12.
 */
public class ListFragmentModel extends BaseModel<ListFragmentPresenter> {

    public ListFragmentModel(ListFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<AnnouncementEntity> getAnnouncements(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getAnnouncements(NetHelper.createBaseArgumentsMap(KeyValueCreator.getAnnouncements(String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }

    public Observable<ComplaintEntity> getComplaints(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getComplaints(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaints("248", "579108042ffc51ee135b8f6648a723d239d6b329", String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }

    public Observable<RepairEntity> getRepairs(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getRepair(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaints("248", "579108042ffc51ee135b8f6648a723d239d6b329", String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }

    public Observable<ComplaintDetailEntity> getComplaintDetail(long id) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getComplaintDetail(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaintDetail("248", "579108042ffc51ee135b8f6648a723d239d6b329", String.valueOf(id))))
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> replyMessage(long id, String content, int type) {

        Observable<TrueEntity> observable;

        if (type == ActionFragment.COMPLAINT) {
            KeyValuePair complaintPair = KeyValueCreator.replyComplaint("248", "579108042ffc51ee135b8f6648a723d239d6b329", content, String.valueOf(id));
            BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);
            observable = RetrofitHelper.<ServiceAPI>createDefault()
                    .replyComplaint(argument)
                    .compose(CommonWrap.wrap());
        } else {
            KeyValuePair repairPair = KeyValueCreator.replyRepair("248", "579108042ffc51ee135b8f6648a723d239d6b329", content, String.valueOf(id));
            BaseArgumentsEntity argument = NetHelper.createBaseArguments(repairPair);
            observable = RetrofitHelper.<ServiceAPI>createDefault()
                    .replyRepair(argument)
                    .compose(CommonWrap.wrap());
        }

        return observable;
    }

    public Observable<RepairDetailEntity> getRepairDetail(long id) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getRepairDetail(NetHelper.createBaseArgumentsMap(KeyValueCreator.getRepairDetail("248", "579108042ffc51ee135b8f6648a723d239d6b329", String.valueOf(id))))
                .compose(CommonWrap.wrap());
    }
}
