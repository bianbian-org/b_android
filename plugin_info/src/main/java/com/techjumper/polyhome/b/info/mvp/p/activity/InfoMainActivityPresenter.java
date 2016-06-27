package com.techjumper.polyhome.b.info.mvp.p.activity;

import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.ReadMessageEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmoreInfoEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.plugincommunicateengine.entity.core.SaveInfoEntity;
import com.techjumper.plugincommunicateengine.utils.GsonUtils;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.UserInfoManager;
import com.techjumper.polyhome.b.info.mvp.m.InfoMainActivityModel;
import com.techjumper.polyhome.b.info.mvp.v.activity.InfoMainActivity;

import java.util.HashMap;
import java.util.Map;

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
//                            else if (o instanceof UserInfoEvent) {
//                                int type = getView().getType();
//                                if (type == NoticeEntity.PROPERTY) {
//                                    getAnnouncements();
//                                } else {
//                                    getList(intentType);
//                                }
//                            }
                        })
        );
//        PluginEngineUtil.initUserInfo();
//
//        PluginEngine.getInstance().registerReceiver((code, message, extras) -> {
//            if (code == PluginEngine.CODE_GET_SAVE_INFO) {
//                SaveInfoEntity saveInfoEntity = GsonUtils.fromJson(message, SaveInfoEntity.class);
//                if (saveInfoEntity == null || saveInfoEntity.getData() == null)
//                    return;
//
//                Log.d("plugin", "name: " + saveInfoEntity.getData().getName());
//                HashMap<String, String> hashMap = saveInfoEntity.getData().getValues();
//                if (hashMap == null || hashMap.size() == 0)
//                    return;
//
//                UserInfoEntity userInfoEntity = new UserInfoEntity();
//
//                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
//                    Log.d("value", entry.getValue());
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//                    if (key.equals("id")) {
//                        userInfoEntity.setId(Long.parseLong(value));
//                    } else if (key.equals("family_name")) {
//                        userInfoEntity.setFamily_name(value);
//                    } else if (key.equals("user_id")) {
//                        userInfoEntity.setUser_id(Long.parseLong(value));
//                    } else if (key.equals("ticket")) {
//                        userInfoEntity.setTicket(value);
//                    } else if (key.equals("has_binding")) {
//                        userInfoEntity.setHas_binding(Integer.parseInt(value));
//                    }
//                }
//
//                UserInfoManager.saveUserInfo(userInfoEntity);
//
//                RxBus.INSTANCE.send(new UserInfoEvent(userInfoEntity));
//            }
//        });
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
