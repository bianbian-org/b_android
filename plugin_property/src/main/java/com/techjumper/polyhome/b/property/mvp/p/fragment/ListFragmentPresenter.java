package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.PropertyMessageDetailEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.m.ListFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ListFragment;

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
            getRepairs(1);
        }
    }

    @OnCheckedChanged(R.id.fl_title_complaint)
    void checkComplaint(boolean check) {
        if (check) {
            getComplaints(1);
        }
    }

    @OnClick(R.id.fl_title_action)
    void action_title() {
        int type = getView().getType();
        PropertyActionEvent propertyActionEvent = new PropertyActionEvent(true);
        propertyActionEvent.setType(type);
        RxBus.INSTANCE.send(propertyActionEvent);
    }

    @OnClick(R.id.lmd_message_send)
    void message_send() {
        String message = getView().getLmdMessageContent().getText().toString();

        if (TextUtils.isEmpty(message.trim())) {
            ToastUtils.show(getView().getContext().getString(R.string.property_send_notnull));
            return;
        }

        long id = getView().getSendId();
        int type = getView().getActionType();

        sendMessage(id, message, type);

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getAnnouncements(1);

        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof PropertyNormalDetailEvent) {
                        PropertyNormalDetailEvent event = (PropertyNormalDetailEvent) o;
                        getView().showLndLayout(event);
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                    } else if (o instanceof PropertyListEvent) {
                        getView().showListLayout();
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
                    } else if (o instanceof PropertyMessageDetailEvent) {
                        PropertyMessageDetailEvent event = (PropertyMessageDetailEvent) o;
                        getMessageDetail(event.getId(), event.getType());
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
                getView().showError(e);
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

    public void getComplaints(int page) {
        addSubscription(model.getComplaints(page)
                .subscribe(new Subscriber<ComplaintEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ComplaintEntity complaintEntity) {
                        if (complaintEntity == null ||
                                complaintEntity.getData() == null ||
                                complaintEntity.getData().getSuggestions() == null)
                            return;

                        getView().getComplaints(complaintEntity.getData().getSuggestions(), page);
                    }
                }));
    }

    public void getRepairs(int page) {
        addSubscription(model.getRepairs(page)
                .subscribe(new Subscriber<RepairEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(RepairEntity repairEntity) {
                        if (repairEntity == null ||
                                repairEntity.getData() == null ||
                                repairEntity.getData().getRepairs() == null)
                            return;

                        getView().getRepairs(repairEntity.getData().getRepairs(), page);
                    }
                }));
    }

    public void getMessageDetail(long id, int type) {
        if (type == PropertyMessageDetailEvent.COMPLAINT) {
            addSubscription(model.getComplaintDetail(id)
                    .subscribe(new Subscriber<ComplaintDetailEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().showError(e);
                        }

                        @Override
                        public void onNext(ComplaintDetailEntity complaintDetailEntity) {
                            if (complaintDetailEntity == null ||
                                    complaintDetailEntity.getData() == null)
                                return;

                            getView().showComplaintDetailLmdLayout(complaintDetailEntity.getData());
                            RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                        }
                    }));
        } else {
            addSubscription(model.getRepairDetail(id)
                    .subscribe(new Subscriber<RepairDetailEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("jj", "");
                            getView().showError(e);
                        }

                        @Override
                        public void onNext(RepairDetailEntity repairDetailEntity) {
                            if (repairDetailEntity == null ||
                                    repairDetailEntity.getData() == null)
                                return;

                                                                                                                                                                                                                                                                               getView().showRepairDetailLmdLayout(repairDetailEntity.getData());
                            RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                        }
                    }));

        }
    }

    public void sendMessage(long id, String content, int type) {
        addSubscription(model.replyMessage(id, content, type)
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (trueEntity == null ||
                                trueEntity.getData() == null ||
                                TextUtils.isEmpty(trueEntity.getData().getResult()))
                            return;

                        if (trueEntity.getData().getResult().equals("true")) {
                            ToastUtils.show(getView().getContext().getString(R.string.property_send_success));
                            getView().sendSuccess();
                        }
                    }
                }));
    }
}