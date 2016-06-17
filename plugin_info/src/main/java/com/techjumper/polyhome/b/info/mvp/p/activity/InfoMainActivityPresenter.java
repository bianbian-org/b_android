package com.techjumper.polyhome.b.info.mvp.p.activity;

import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.ReadMessageEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmoreInfoEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.mvp.m.InfoMainActivityModel;
import com.techjumper.polyhome.b.info.mvp.v.activity.InfoMainActivity;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/29.
 */
public class InfoMainActivityPresenter extends AppBaseActivityPresenter<InfoMainActivity> {

    InfoMainActivityModel infoMainActivityModel = new InfoMainActivityModel(this);
    private int pageNo = 1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        int intentType = getView().getType();
        if (intentType == NoticeEntity.PROPERTY) {
            getAnnouncements();
        } else {
            getList(intentType);
        }

        addSubscription(
                RxBus.INSTANCE.asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof ReadMessageEvent) {
                                readMessage(((ReadMessageEvent) o).getId());
                            } else if (o instanceof InfoTypeEvent) {
                                pageNo = 1;

                                int type = ((InfoTypeEvent) o).getType();
                                if (type == NoticeEntity.PROPERTY) {
                                    getAnnouncements();
                                } else {
                                    getList(type);
                                }
                            } else if (o instanceof PropertyNormalDetailEvent) {
                                PropertyNormalDetailEvent event = (PropertyNormalDetailEvent) o;
                                getView().showLndLayout(event);
                            } else if (o instanceof LoadmoreInfoEvent) {
                                pageNo++;

                                int type = ((LoadmoreInfoEvent) o).getType();
                                if (type == NoticeEntity.PROPERTY) {
                                    getAnnouncements();
                                } else {
                                    getList(type);
                                }
                            }
                        })
        );
    }

    @OnClick(R.id.bottom_home)
    void home() {
        getView().finish();
    }

    public void getList(int type) {
        addSubscription(infoMainActivityModel.getInfo(type, pageNo).subscribe(new Subscriber<InfoEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e);
            }

            @Override
            public void onNext(InfoEntity infoEntity) {
                if (infoEntity != null &&
                        infoEntity.getData() != null &&
                        infoEntity.getData().getMessages() != null &&
                        infoEntity.getData().getMessages().size() > 0) {
                    getView().getList(infoEntity.getData().getMessages(), pageNo);
                }
            }
        }));
    }

    public void getAnnouncements() {
        addSubscription(infoMainActivityModel.getAnnouncements(pageNo).subscribe(new Subscriber<AnnouncementEntity>() {
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

                getView().getAnnouncements(announcementEntity.getData().getNotices(), pageNo);
            }
        }));
    }

    public void readMessage(long message_id) {
        addSubscription(infoMainActivityModel.readMessage(message_id).subscribe(new Subscriber<TrueEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TrueEntity trueEntity) {
                if (trueEntity != null &&
                        trueEntity.getData() != null) {
                    getView().readMessage(trueEntity.getData().getResult());
                }
            }
        }));
    }
}
