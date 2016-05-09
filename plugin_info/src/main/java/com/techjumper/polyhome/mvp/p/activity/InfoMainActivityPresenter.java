package com.techjumper.polyhome.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.polyhome.InfoEntityTemporary;
import com.techjumper.polyhome.mvp.m.InfoMainActivityModel;
import com.techjumper.polyhome.mvp.v.activity.InfoMainActivity;

import java.util.List;

import rx.Observer;
import rx.Subscriber;

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
    }

    public void getList(){
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
                        infoEntity.getData().getMessages().size() > 0){
                    getView().getList(infoEntity.getData().getMessages());
                }
            }
        }));
    }
}
