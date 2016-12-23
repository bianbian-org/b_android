package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.DnakeLockData;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.VillageLockEntity;
import com.techjumper.polyhomeb.mvp.m.DnakeActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.DnakeLockActivity;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DnakeActivityPresenter extends AppBaseActivityPresenter<DnakeLockActivity> {

    private Subscription mSubs1, mSubs2;

    private DnakeActivityModel mModel = new DnakeActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getLocks();
    }

    private void getLocks() {
        getView().showLoading(false);
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getVillageLocks()
                        .subscribe(new Observer<VillageLockEntity>() {
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
                            public void onNext(VillageLockEntity villageLockEntity) {
                                if (!processNetworkResult(villageLockEntity)) return;
                                if (villageLockEntity != null && villageLockEntity.getData() != null) {
                                    getView().showData(mModel.processDatas(villageLockEntity));
                                } else {
                                    ToastUtils.show(getView().getString(R.string.no_dnake_lock));
                                }
                            }
                        }));
    }

    public void onUnlockClick(DnakeLockData data) {
        if (data == null) return;
        int id = data.getId();
        String name = data.getName();
        unlock(id, name);
    }

    private void unlock(int id, String name) {
        getView().showLoading(false);
//        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.unlock(id)
                        .subscribe(new Observer<TrueEntity>() {
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
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (trueEntity == null || trueEntity.getData() == null
                                        || !"true".equals(trueEntity.getData().getResult())) {
                                    ToastUtils.show(getView().getString(R.string.ble_unlock_failed));
                                    return;
                                } else {
                                    ToastUtils.show(String.format(getView().getString(R.string.open_x_success), name));
                                }
                            }
                        }));
    }
}
