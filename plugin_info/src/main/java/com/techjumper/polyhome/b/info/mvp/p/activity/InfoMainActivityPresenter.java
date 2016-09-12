package com.techjumper.polyhome.b.info.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.HeartbeatEvent;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.ReadMessageEvent;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmoreInfoEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.UserInfoManager;
import com.techjumper.polyhome.b.info.mvp.m.InfoMainActivityModel;
import com.techjumper.polyhome.b.info.mvp.v.activity.InfoMainActivity;
import com.techjumper.polyhome.b.info.net.NetHelper;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/29.
 */
public class InfoMainActivityPresenter extends AppBaseActivityPresenter<InfoMainActivity> {

    private int intentType;
    private long time;
    private Timer timer = new Timer();

    InfoMainActivityModel infoMainActivityModel = new InfoMainActivityModel(this);
    private int pageNo = 1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        time = getView().getTime();

        if (time == 0L) {
            time = System.currentTimeMillis() / 1000;
        }
        getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));


        intentType = getView().getType();
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
                                readMessage(((ReadMessageEvent) o).getId(), ((ReadMessageEvent) o).getType());
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
                            } else if (o instanceof TimeEvent) {
                                Log.d("time", "更新时间");
                                if (getView().getBottomDate() != null) {
                                    getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                                }
                            } else if (o instanceof HeartbeatEvent) {
                                HeartbeatEvent event = (HeartbeatEvent) o;
                                if (event != null) {
                                    time = event.getTime();
                                    Log.d("infosubmitOnline", "心跳时间:" + time);
                                    getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                                }
                            }
                        })
        );

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time++;
                String second = CommonDateUtil.getSecond(time);
                Log.d("infosubmitOnline", "second: " + second);
                if (second.equals("00")) {
                    RxBus.INSTANCE.send(new TimeEvent());
                }
            }
        }, 0, 1000);
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
                if (infoEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                    getAgainList(type);
                    return;
                }

                if (!processNetworkResult(infoEntity, false))
                    return;

                if (infoEntity != null &&
                        infoEntity.getData() != null &&
                        infoEntity.getData().getResult() != null &&
                        infoEntity.getData().getResult().getMessages() != null) {
                    getView().getList(infoEntity.getData().getResult(), pageNo);
                }
            }
        }));
    }

    private void getAgainList(int type) {
        addSubscription(infoMainActivityModel.submitOnline()
                .flatMap(heartbeatEntity -> {
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    return infoMainActivityModel.getInfo(type, pageNo);
                }).subscribe(new Subscriber<InfoEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(InfoEntity infoEntity) {
                        if (!processNetworkResult(infoEntity, false))
                            return;

                        if (infoEntity != null &&
                                infoEntity.getData() != null &&
                                infoEntity.getData().getResult() != null &&
                                infoEntity.getData().getResult().getMessages() != null) {
                            getView().getList(infoEntity.getData().getResult(), pageNo);
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
                if (announcementEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                    Log.d("gonggao", "ticket失效 之前的ticket是:" + UserInfoManager.getTicket());
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
        Log.d("gonggao", "开始执行getAgainAnnouncements");
        addSubscription(infoMainActivityModel.submitOnline()
                .flatMap(heartbeatEntity -> {
                    Log.d("gonggao", "heartbeatEntity:" + heartbeatEntity);
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    Log.d("gonggao", "现在的ticket 1是:" + UserInfoManager.getTicket());
                    return infoMainActivityModel.getAnnouncements(pageNo);
                }).subscribe(new Subscriber<AnnouncementEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(AnnouncementEntity announcementEntity) {
                        Log.d("gonggao", "现在的ticket 2是:" + UserInfoManager.getTicket());

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

    public void readMessage(long message_id, int type) {
        addSubscription(infoMainActivityModel.readMessage(message_id).subscribe(new Subscriber<TrueEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e);
            }

            @Override
            public void onNext(TrueEntity trueEntity) {
                if (trueEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                    readAgainMessage(message_id, type);
                    return;
                }
                if (!processNetworkResult(trueEntity, false))
                    return;

                if (trueEntity != null &&
                        trueEntity.getData() != null) {
                    getView().readMessage(trueEntity.getData().getResult(), type);
                }
            }
        }));
    }

    private void readAgainMessage(long message_id, int type) {
        addSubscription(infoMainActivityModel.submitOnline()
                .flatMap(heartbeatEntity -> {
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    return infoMainActivityModel.readMessage(message_id);
                }).subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (!processNetworkResult(trueEntity, false))
                            return;

                        if (trueEntity != null &&
                                trueEntity.getData() != null) {
                            getView().readMessage(trueEntity.getData().getResult(), type);
                        }
                    }
                }));
    }
}
