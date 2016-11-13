package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.RenameRoomEntity;
import com.techjumper.polyhomeb.entity.event.RenameRoomEvent;
import com.techjumper.polyhomeb.mvp.m.RenameRoomActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.RenameRoomActivity;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RenameRoomActivityPresenter extends AppBaseActivityPresenter<RenameRoomActivity> {

    private RenameRoomActivityModel mModel = new RenameRoomActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public String getRoomName() {
        return mModel.getRoomName();
    }

    @OnClick(R.id.tv_ok)
    public void onClick(View view) {
        String roomName = getView().getEtNewRoom().getEditableText().toString();
        if (TextUtils.isEmpty(roomName)) {
            ToastUtils.show(getView().getString(R.string.can_not_be_null));
            return;
        }
        if (roomName.equals(mModel.getRoomName())) {
            KeyboardUtils.closeKeyboard(getView().getEtNewRoom());
            getView().finish();
            return;
        }

        renameRoom(roomName);
    }

    private void renameRoom(String room_name) {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.renameRoom(room_name)
                        .subscribe(new Observer<RenameRoomEntity>() {
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
                            public void onNext(RenameRoomEntity renameRoomEntity) {
                                if (!processNetworkResult(renameRoomEntity)) return;
                                if (renameRoomEntity == null
                                        || renameRoomEntity.getData() == null
                                        || TextUtils.isEmpty(renameRoomEntity.getData().getId())) {
                                    ToastUtils.show(getView().getString(R.string.error_rename_room));
                                    return;
                                }
                                RxBus.INSTANCE.send(new RenameRoomEvent(renameRoomEntity.getData().getId()
                                        , renameRoomEntity.getData().getRoom_name()));
                                KeyboardUtils.closeKeyboard(getView().getEtNewRoom());
                                getView().finish();
                            }
                        }));
    }
}
