package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.RoomManageAdapter;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRoomData;
import com.techjumper.polyhomeb.adapter.recycler_Data.RoomManageData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRoomBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.RoomManageBean;
import com.techjumper.polyhomeb.entity.C_AllRoomEntity;
import com.techjumper.polyhomeb.entity.event.NewRoomEvent;
import com.techjumper.polyhomeb.entity.event.RenameRoomEvent;
import com.techjumper.polyhomeb.mvp.m.RoomManageActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewRoomActivity;
import com.techjumper.polyhomeb.mvp.v.activity.RenameRoomActivity;
import com.techjumper.polyhomeb.mvp.v.activity.RoomManageActivity;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RoomManageActivityPresenter extends AppBaseActivityPresenter<RoomManageActivity> {

    private Subscription mSubs1, mSubs2, mSubs3;
    private boolean mHasRoom = true;
    private boolean mIsEditMode = false;

    public static final String KEY_ROOM_ID = "key_room_id";
    public static final String KEY_ROOM_NAME = "key_room_name";

    private RoomManageActivityModel mModel = new RoomManageActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        getAllRoomsData();
        getRxMassages();
    }

    private void getRxMassages() {
        addSubscription(
                mSubs3 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof NewRoomEvent) {
                                NewRoomEvent event = (NewRoomEvent) o;
                                String id = event.getId();
                                String name = event.getName();
                                //此处不用adapter的notifyItemInsert(int position)是因为这个rv列表包含了
                                //多种item，主要是空白View，分割线，真正的实体数据和"新建房间item",如果用
                                //insert方法，处理的逻辑要比直接重新请求数据还要多，所以这里直接请求最新数据
                                getAllRoomsData();
                            } else if (o instanceof RenameRoomEvent) {
                                RenameRoomEvent event = (RenameRoomEvent) o;
                                String id = event.getId();
                                String name = event.getName();
                                renameSuccess(id, name);
                            }
                        })
        );
    }

    public void onTitleRightClick() {
        if (!mHasRoom || getView().getAdapter() == null) return;
        List<DisplayBean> data = getView().getAdapter().getData();
        for (int i = 0; i < data.size(); i++) {
            DisplayBean bean = data.get(i);
            if (bean instanceof RoomManageBean) {
                RoomManageBean manageBean = (RoomManageBean) bean;
                RoomManageData manageData = manageBean.getData();
                manageData.setDeleteMode(!mIsEditMode);
            }
            if (bean instanceof NewRoomBean) {
                NewRoomBean newRoomBean = (NewRoomBean) bean;
                NewRoomData newRoomData = newRoomBean.getData();
                newRoomData.setCanShow(mIsEditMode);
            }
            getView().getAdapter().notifyDataSetChanged();
        }
        mIsEditMode = !mIsEditMode;
    }

    public void onRoomItemClick(RoomManageBean data) {
        RoomManageData data1 = data.getData();
        String room_id = data1.getRoom_id();
        String room_name = data1.getRoom_name();

        if (mIsEditMode) {
            showConfirmDialog(room_id, room_name);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_ROOM_ID, room_id);
            bundle.putString(KEY_ROOM_NAME, room_name);
            new AcHelper.Builder(getView()).extra(bundle).target(RenameRoomActivity.class).start();
        }
    }

    public void onNewRoomItemClick(NewRoomBean data) {
        new AcHelper.Builder(getView()).target(NewRoomActivity.class).start();
    }

    private void getAllRoomsData() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getAllRooms()
                        .subscribe(new Observer<C_AllRoomEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                                mHasRoom = false;
                                mIsEditMode = false;
                            }

                            @Override
                            public void onNext(C_AllRoomEntity c_allRoomEntity) {
                                if (!processNetworkResult(c_allRoomEntity)) return;
                                if (c_allRoomEntity == null
                                        || c_allRoomEntity.getData() == null
                                        || c_allRoomEntity.getData().getResult() == null
                                        || c_allRoomEntity.getData().getResult().size() == 0) {
                                    ToastUtils.show(getView().getString(R.string.no_room));
                                    mHasRoom = false;
                                    getView().onRoomsDataReceive(mModel.noData());
                                    return;
                                }
                                mHasRoom = true;
                                mIsEditMode = false;
                                getView().onRoomsDataReceive(mModel.processDatas(c_allRoomEntity));
                            }
                        }));
    }

    public boolean isEditMode() {
        return mIsEditMode;
    }

    private void showConfirmDialog(String room_id, String room_name) {
        DialogUtils.getBuilder(getView())
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .content(String.format(getView().getString(R.string.confirm_delete_x_room), room_name))
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            deleteRoom(room_id, room_name);
                            break;
                    }
                })
                .show();
    }

    /**
     * 方案1，删除后请求最新的房间数据列表
     */
    private void deleteRoom(String room_id, String room_name) {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.deleteRoom(room_id)
                        .flatMap(trueEntity -> {
                            if (trueEntity == null
                                    || trueEntity.getData() == null
                                    || TextUtils.isEmpty(trueEntity.getData().getResult())
                                    || !"true".equalsIgnoreCase(trueEntity.getData().getResult())) {
                                ToastUtils.show(String.format(
                                        getView().getString(R.string.delete_x_failed), room_name));
                            }
                            return mModel.getAllRooms();
                        })
                        .subscribe(new Observer<C_AllRoomEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                                mHasRoom = false;
                                mIsEditMode = false;
                            }

                            @Override
                            public void onNext(C_AllRoomEntity c_allRoomEntity) {
                                if (!processNetworkResult(c_allRoomEntity)) return;
                                if (c_allRoomEntity == null
                                        || c_allRoomEntity.getData() == null
                                        || c_allRoomEntity.getData().getResult() == null
                                        || c_allRoomEntity.getData().getResult().size() == 0) {
                                    ToastUtils.show(getView().getString(R.string.no_room));
                                    mHasRoom = false;
                                    mIsEditMode = false;
                                    getView().onRoomsDataReceive(mModel.noData());
                                    return;
                                }
                                mHasRoom = true;
                                mIsEditMode = false;
                                getView().onRoomsDataReceive(mModel.processDatas(c_allRoomEntity));
                            }
                        }));
    }

    /**
     * 方案2，删除后只是刷新界面，不请求数据(但是分割线没有办法做处理，毕竟分割线也是item)
     */
//    private void deleteRoom(String room_id, String room_name) {
//        RxUtils.unsubscribeIfNotNull(mSubs2);
//        addSubscription(
//                mSubs2 = mModel.deleteRoom(room_id)
//                        .subscribe(new Observer<TrueEntity>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                getView().showError(e);
//                            }
//
//                            @Override
//                            public void onNext(TrueEntity trueEntity) {
//                                if (!processNetworkResult(trueEntity)) return;
//                                if (trueEntity == null
//                                        || trueEntity.getData() == null
//                                        || TextUtils.isEmpty(trueEntity.getData().getResult())) {
//                                    ToastUtils.show(String.format(getView().getString(R.string.delete_x_failed)
//                                            , room_name));
//                                    return;
//                                }
//
//                                if (getView().getAdapter() == null) return;
//                                RoomManageAdapter adapter = getView().getAdapter();
//                                List<DisplayBean> data = adapter.getData();
//                                for (int i = 0; i < data.size(); i++) {
//                                    DisplayBean bean = data.get(i);
//                                    if (bean instanceof RoomManageBean) {
//                                        RoomManageBean manageBean = (RoomManageBean) bean;
//                                        RoomManageData data1 = manageBean.getData();
//                                        if (data1.getRoom_id().equalsIgnoreCase(room_id)) {
//                                            adapter.notifyItemRemoved(i);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        }));
//    }
    private void renameSuccess(String name, String id) {
        RoomManageAdapter adapter = getView().getAdapter();
        if (adapter == null) return;
        List<DisplayBean> data = adapter.getData();
        for (int i = 0; i < data.size(); i++) {
            DisplayBean bean = data.get(i);
            if (bean instanceof RoomManageBean) {
                RoomManageData manageData = ((RoomManageBean) bean).getData();
                if (manageData.getRoom_id().equalsIgnoreCase(id)) {
                    manageData.setRoom_name(name);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }
}
