package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.adapter.databean.MyVillageFamilyBean;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.entity.event.BLEInfoChangedEvent;
import com.techjumper.polyhomeb.entity.event.ChangeVillageIdRefreshEvent;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.mvp.m.MyVillageFamilyActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChooseVillageFamilyActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MyVillageFamilyActivity;
import com.techjumper.polyhomeb.user.UserManager;

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

    private Subscription mSubs1, mSubs2, mSubs3;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getFamilyAndVillage();
        onClickItem();
    }

    public void onTitleRightClick() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_COME_FROM, Constant.VALUE_COME_FROM);
        new AcHelper
                .Builder(getView())
                .extra(bundle)
                .target(ChooseVillageFamilyActivity.class)
                .start();
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
                                    getView().showData(mModel.processData(userFamiliesAndVillagesEntity));
                                }
                            }
                        }));
    }

    private void onClickItem() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE.asObservable()
                        .subscribe(o -> {
                            if (o instanceof ChooseFamilyVillageEvent) {
                                ChooseFamilyVillageEvent event = (ChooseFamilyVillageEvent) o;
                                int position = event.getPosition();  //被点击的家庭或者小区在RV中的position
                                List<DisplayBean> data = getView().getAdapter().getData();
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i) instanceof MyVillageFamilyBean) {
                                        if (i == position) {
                                            ((MyVillageFamilyBean) data.get(i)).getData().setChoosed(true);
                                        } else {
                                            ((MyVillageFamilyBean) data.get(i)).getData().setChoosed(false);
                                        }
                                    }
                                }
                                getView().getAdapter().notifyDataSetChanged();
                                RxBus.INSTANCE.send(new ChangeVillageIdRefreshEvent());
                                getBLEDoorInfo();
                            }
                        }));
    }

    private void getBLEDoorInfo() {
        boolean isFamily = UserManager.INSTANCE.isFamily();//true为家庭,false为小区
        String family_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID);
        String village_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
        if (!isFamily) {
            family_id = "";
        }
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.getBLEDoorInfo(village_id, family_id)
                        .subscribe(new Observer<BluetoothLockDoorInfoEntity>() {
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
                            public void onNext(BluetoothLockDoorInfoEntity bluetoothLockDoorInfoEntity) {
                                if (!processNetworkResult(bluetoothLockDoorInfoEntity)) return;
                                if (bluetoothLockDoorInfoEntity != null
                                        && bluetoothLockDoorInfoEntity.getData() != null) {
                                    //切换家庭或者小区之后，发送消息给HomeFragment,刷新首页数据
                                    RxBus.INSTANCE.send(new BLEInfoChangedEvent());
                                    UserManager.INSTANCE.saveBLEInfo(bluetoothLockDoorInfoEntity);
                                }
                            }
                        }));
    }


}
