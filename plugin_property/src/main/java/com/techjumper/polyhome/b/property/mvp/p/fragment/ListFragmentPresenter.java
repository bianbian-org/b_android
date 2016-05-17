package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.hehe.ComplaintHehe;
import com.techjumper.polyhome.b.property.hehe.RepairHehe;
import com.techjumper.polyhome.b.property.mvp.m.ListFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ListFragment;

import java.util.List;

import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/12.
 */
public class ListFragmentPresenter extends AppBaseFragmentPresenter<ListFragment> {
    private ListFragmentModel model = new ListFragmentModel(this);

    @OnCheckedChanged(R.id.fl_title_announcement)
    void checkAnnouncement(boolean check) {
        if (check) {
            getAnnouncements(1);
        }
    }

    @OnCheckedChanged(R.id.fl_title_repair)
    void checkRepair(boolean check) {
        if (check) {
            getView().getRepairHehes(getRepairHehes());
        }
    }

    @OnCheckedChanged(R.id.fl_title_complaint)
    void checkComplaint(boolean check) {
        if (check) {
            getView().getComplaintHehes(getComplaintHehes());
        }
    }

    @OnClick(R.id.fl_title_action)
    void action_title() {
        int type = getView().getType();
        PropertyActionEvent propertyActionEvent = new PropertyActionEvent(true);
        propertyActionEvent.setType(type);
        RxBus.INSTANCE.send(propertyActionEvent);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getAnnouncements(1);

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof PropertyNormalDetailEvent) {
                        PropertyNormalDetailEvent event = (PropertyNormalDetailEvent) o;
                        getView().showLndLayout(event);
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                    } else if (o instanceof PropertyListEvent) {
                        getView().showListLayout();
                    }
                }));
    }

    public void getAnnouncements(int page) {
        addSubscription(model.getAnnouncements(page).subscribe(new Subscriber<AnnouncementEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(AnnouncementEntity announcementEntity) {
                if (announcementEntity == null ||
                        announcementEntity.getData() == null ||
                        announcementEntity.getData().getNotices() == null)
                    return;

                getView().getAnnouncements(announcementEntity.getData().getNotices(), page);
            }
        }));
    }

    public List<ComplaintHehe> getComplaintHehes() {
        List<ComplaintHehe> complaintHehes = model.getComplaintHehes();
        ComplaintHehe complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        return complaintHehes;
    }

    public List<RepairHehe> getRepairHehes() {
        List<RepairHehe> complaintHehes = model.getRepairHehes();
        RepairHehe complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        return complaintHehes;
    }
}
