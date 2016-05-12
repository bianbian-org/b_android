package com.techjumper.polyhome.b.info.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.InfoTypeEvent;
import com.techjumper.commonres.entity.event.ReadMessageEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.info.mvp.m.InfoMainActivityModel;
import com.techjumper.polyhome.b.info.mvp.v.activity.InfoMainActivity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/29.
 */
public class InfoMainActivityPresenter extends AppBaseActivityPresenter<InfoMainActivity> {
    InfoMainActivityModel infoMainActivityModel = new InfoMainActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getList();

        addSubscription(
                RxBus.INSTANCE.asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof ReadMessageEvent) {
                                readMessage(((ReadMessageEvent) o).getId());
                            } else if (o instanceof InfoTypeEvent) {
                                int type = ((InfoTypeEvent) o).getType();
                                if (type != -1) {
                                    getList(type);
                                } else {
                                    getList();
                                }
                            }
                        })
        );
    }

    public void getList() {
        addSubscription(infoMainActivityModel.getInfo(1).subscribe(new Subscriber<InfoEntity>() {
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
                    getView().getList(infoEntity.getData().getMessages());
                }
            }
        }));
    }

    public void getList(int type) {
        addSubscription(infoMainActivityModel.getInfo(type, 1).subscribe(new Subscriber<InfoEntity>() {
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
                    getView().getList(infoEntity.getData().getMessages());
                }
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