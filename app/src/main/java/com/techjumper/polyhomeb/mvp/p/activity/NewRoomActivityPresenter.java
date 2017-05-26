package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.NewRoomEntity;
import com.techjumper.polyhomeb.entity.event.NewRoomEvent;
import com.techjumper.polyhomeb.mvp.m.NewRoomActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewRoomActivity;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class NewRoomActivityPresenter extends AppBaseActivityPresenter<NewRoomActivity> {

    private NewRoomActivityModel mModel = new NewRoomActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_ok)
    public void onClick(View view) {
        String roomName = getView().getEtNewRoom().getEditableText().toString();
        if (TextUtils.isEmpty(roomName)) {
            ToastUtils.show(getView().getString(R.string.can_not_be_null));
            return;
        }

        newRoom(roomName);
    }

    private void newRoom(String room_name) {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.newRoom(room_name)
                        .subscribe(new Observer<NewRoomEntity>() {
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
                            public void onNext(NewRoomEntity newRoomEntity) {
                                if (!processNetworkResult(newRoomEntity)) return;
                                if (newRoomEntity == null
                                        || newRoomEntity.getData() == null
                                        || TextUtils.isEmpty(newRoomEntity.getData().getRoom_name())
                                        || TextUtils.isEmpty(newRoomEntity.getData().getFamily_id())) {
                                    if (!TextUtils.isEmpty(newRoomEntity.getError_msg())) {
                                        ToastUtils.show(String.format(
                                                getView().getString(R.string.error_new_room_x)
                                                , newRoomEntity.getError_msg()));
                                    } else {
                                        ToastUtils.show(getView().getString(R.string.error_new_room));
                                    }
                                    return;
                                }

                                RxBus.INSTANCE.send(new NewRoomEvent(newRoomEntity.getData().getRoom_name()
                                        , String.valueOf(newRoomEntity.getData().getRoom_id())));
                                getView().finish();
                            }
                        }));
    }
}
