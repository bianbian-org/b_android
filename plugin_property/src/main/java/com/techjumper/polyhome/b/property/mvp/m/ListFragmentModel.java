package com.techjumper.polyhome.b.property.mvp.m;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.hehe.AnnounHehe;
import com.techjumper.polyhome.b.property.hehe.ComplaintHehe;
import com.techjumper.polyhome.b.property.hehe.RepairHehe;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ListFragmentPresenter;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by kevin on 16/5/12.
 */
public class ListFragmentModel extends BaseModel<ListFragmentPresenter> {

    public ListFragmentModel(ListFragmentPresenter presenter) {
        super(presenter);
    }

    public List<AnnounHehe> getAnnounHehes() {
        return new ArrayList<AnnounHehe>();
    }

    public List<ComplaintHehe> getComplaintHehes() {
        return new ArrayList<ComplaintHehe>();
    }

    public List<RepairHehe> getRepairHehes() {
        return new ArrayList<RepairHehe>();
    }

    public Observable<AnnouncementEntity> getAnnouncements(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getAnnouncements(NetHelper.createBaseArgumentsMap(KeyValueCreator.getAnnouncements(String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }

    public Observable<ComplaintEntity> getComplaints(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getComplaints(NetHelper.createBaseArgumentsMap(KeyValueCreator.getComplaints("248", "ded9133b9df5817e5f0220e0118b8ec8d8dbd8ea",String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }
}
