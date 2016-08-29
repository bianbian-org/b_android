package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.adapter.recycler_Data.MyVillageFamilyData;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.entity.event.ChooseVillageFamilyEvent;
import com.techjumper.polyhomeb.mvp.m.MyVillageFamilyActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChooseVillageFamilyActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MyVillageFamilyActivity;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MyVillageFamilyActivityPresenter extends AppBaseActivityPresenter<MyVillageFamilyActivity> {

    private MyVillageFamilyActivityModel mModel = new MyVillageFamilyActivityModel(this);

    private Subscription mSubs1, mSubs2;

    private UserFamiliesAndVillagesEntity entity;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getFamilyAndVillage();
        changeChoosedItem();
    }

    private void changeChoosedItem() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Object o) {
                                if (o instanceof ChooseVillageFamilyEvent) {
                                    ChooseVillageFamilyEvent event = (ChooseVillageFamilyEvent) o;
                                    int position = event.getPosition();
                                    List<DisplayBean> data = getData();
                                    for (int i = 0; i < data.size(); i++) {
                                        DisplayBean displayBean = data.get(i);
                                        if (displayBean instanceof MyVillageFamilyData) {
                                            if (i == position) {
                                                ((MyVillageFamilyData) displayBean).setChoosed(true);
                                            } else {
                                                ((MyVillageFamilyData) displayBean).setChoosed(false);
                                            }
                                        }
                                    }
                                    getView().getAdapter().loadData(getData());
                                    getView().getAdapter().notifyDataSetChanged();
                                }
                            }
                        }));
    }

    public void onTitleRightClick() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_COME_FROM, Constant.VALUE_COME_FROM);
        new AcHelper.Builder(getView()).extra(bundle).target(ChooseVillageFamilyActivity.class).start();
    }

    private void getFamilyAndVillage() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getFamilyAndVillage()
                        .subscribe(new Observer<UserFamiliesAndVillagesEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                            }

                            @Override
                            public void onNext(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {
                                getView().dismissLoading();
                                if (!processNetworkResult(userFamiliesAndVillagesEntity)) return;
                                if (userFamiliesAndVillagesEntity.getData() != null) {
                                    entity = userFamiliesAndVillagesEntity;
                                    mModel.processData(userFamiliesAndVillagesEntity);
                                    getView().showData();
                                }
                            }
                        }));
    }

    public List<DisplayBean> getData() {
        if (entity != null) {
            mModel.processData(entity);
        }
        return mModel.getDisplayBeen();
    }
}
