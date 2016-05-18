package com.techjumper.polyhome.b.property.mvp.m;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ListFragmentPresenter;
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
                .getComplaints(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaints("248", "ded9133b9df5817e5f0220e0118b8ec8d8dbd8ea", String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }

    public Observable<RepairEntity> getRepairs(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getRepair(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaints("248", "ded9133b9df5817e5f0220e0118b8ec8d8dbd8ea", String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }

    public Observable<ComplaintDetailEntity> getComplaintDetail(long id) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getComplaintDetail(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaintDetail("248", "ded9133b9df5817e5f0220e0118b8ec8d8dbd8ea", String.valueOf(id))))
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> replyComplaint(long id, String content) {
        KeyValuePair complaintPair = KeyValueCreator.replyComplaint("248", "ded9133b9df5817e5f0220e0118b8ec8d8dbd8ea", content, String.valueOf(id));
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(complaintPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .replyComplaint(argument)
                .compose(CommonWrap.wrap());
    }
}
