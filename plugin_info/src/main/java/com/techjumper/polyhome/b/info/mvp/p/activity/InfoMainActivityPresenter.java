package com.techjumper.polyhome.b.info.mvp.p.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;

import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.ReadMessageEvent;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmoreInfoEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.UserInfoManager;
import com.techjumper.polyhome.b.info.mvp.m.InfoMainActivityModel;
import com.techjumper.polyhome.b.info.mvp.v.activity.InfoMainActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/29.
 */
public class InfoMainActivityPresenter extends AppBaseActivityPresenter<InfoMainActivity> {

    private int intentType;

//    @OnCheckedChanged(R.id.title_announcement)
//    void announcement(boolean check) {
//        if (check) {
//            getAnnouncements();
//        }
//    }
//
//    @OnCheckedChanged(R.id.title_system)
//    void titleSystem(boolean check) {
//        if (check) {
//            getList(intentType);
//        }
//    }
//
//    @OnCheckedChanged(R.id.title_order)
//    void titleOrder(boolean check) {
//        if (check) {
//            getList(intentType);
//        }
//    }
//
//    @OnCheckedChanged(R.id.title_medical)
//    void titleMedical(boolean check) {
//        if (check) {
//            getList(intentType);
//        }
//    }

    InfoMainActivityModel infoMainActivityModel = new InfoMainActivityModel(this);
    private int pageNo = 1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
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
                            } else if (o instanceof TimeEvent) {
                                Log.d("time", "更新时间");
                                if (getView().getBottomDate() != null) {
                                    getView().getBottomDate().setText(CommonDateUtil.getTitleDate());
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
                if (!processNetworkResult(infoEntity, false))
                    return;

                if (infoEntity != null &&
                        infoEntity.getData() != null &&
                        infoEntity.getData().getResult() != null &&
                        infoEntity.getData().getResult().getMessages() != null) {
                    getView().getList(infoEntity.getData().getResult().getMessages(), pageNo);
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
                if (!processNetworkResult(trueEntity, false))
                    return;

                if (trueEntity != null &&
                        trueEntity.getData() != null) {
                    getView().readMessage(trueEntity.getData().getResult());
                }
            }
        }));
    }
}
