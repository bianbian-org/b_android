package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.m.AnnouncementFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.mvp.v.fragment.AnnouncementFragment;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ComplaintFragment;
import com.techjumper.polyhome.b.property.net.NetHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/10/27.
 */

public class AnnouncementPresenter extends AppBaseFragmentPresenter<AnnouncementFragment> {

    private AnnouncementFragmentModel model = new AnnouncementFragmentModel(this);
    private int pageNo = 1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getAnnouncements();

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof LoadmorePresenterEvent) {
                        LoadmorePresenterEvent event = (LoadmorePresenterEvent) o;
                        int type = event.getType();
                        pageNo++;
                        if (type == MainActivity.ANNOUNCEMENT) {
                            getAnnouncements();
                        }
                    } else if (o instanceof PropertyNormalDetailEvent) {
                        PropertyNormalDetailEvent event = (PropertyNormalDetailEvent) o;
                        getView().showLndLayout(event);
                    } else if (o instanceof PropertyListEvent) {
                        if (getView().getType() == MainActivity.ANNOUNCEMENT) {
                            getAnnouncements();
                        }
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
                    }
                }));
    }

    public void getAnnouncements() {
        addSubscription(model.getAnnouncements(pageNo).subscribe(new Subscriber<AnnouncementEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e);
            }

            @Override
            public void onNext(AnnouncementEntity announcementEntity) {
                if (announcementEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                    getAgainAnnouncements();
                    return;
                }

                if (!processNetworkResult(announcementEntity, false))
                    return;

                if (announcementEntity == null ||
                        announcementEntity.getData() == null ||
                        announcementEntity.getData().getNotices() == null)
                    return;

                getView().getAnnouncements(announcementEntity.getData().getNotices(), pageNo);
            }
        }));
    }

    private void getAgainAnnouncements() {
        addSubscription(getSubmitOnline(model.getAnnouncements(pageNo)).subscribe(new Subscriber<AnnouncementEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e);
            }

            @Override
            public void onNext(AnnouncementEntity announcementEntity) {
                if (!processNetworkResult(announcementEntity, false))
                    return;
//
                if (announcementEntity == null ||
                        announcementEntity.getData() == null ||
                        announcementEntity.getData().getNotices() == null)
                    return;

                getView().getAnnouncements(announcementEntity.getData().getNotices(), pageNo);
            }
        }));
    }

    private Observable<AnnouncementEntity> getSubmitOnline(Observable<AnnouncementEntity> observable) {
        return model.submitOnline()
                .flatMap(heartbeatEntity -> {
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    return observable;
                });
    }
}
