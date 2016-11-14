package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberDetailBean;
import com.techjumper.polyhomeb.entity.C_AllRoomEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.m.MemberDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MemberDetailActivity;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MemberDetailActivityPresenter extends AppBaseActivityPresenter<MemberDetailActivity> {

    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;

    private MemberDetailActivityModel mModel = new MemberDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        getAllRoomsData();
    }

    private void getAllRoomsData() {
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.getAllRooms()
                        .subscribe(new Observer<C_AllRoomEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                            }

                            @Override
                            public void onNext(C_AllRoomEntity c_allRoomEntity) {
                                if (!processNetworkResult(c_allRoomEntity)) return;
                                if (c_allRoomEntity == null || c_allRoomEntity.getData() == null
                                        || c_allRoomEntity.getData().getResult() == null
                                        || c_allRoomEntity.getData().getResult().size() == 0) {
                                    ToastUtils.show(getView().getString(R.string.no_room));
                                    getView().onMemberDetailDataReceive(mModel.loadData(null));
                                    return;
                                }
                                getView().onMemberDetailDataReceive(mModel.loadData(c_allRoomEntity));
                            }
                        }));
    }

    public String getMemberName() {
        return mModel.getMemberName();
    }

    public void onItemCheckedChange(boolean isChecked, MemberDetailBean dataBean, CompoundButton buttonView) {
        if (dataBean == null || dataBean.getData() == null || TextUtils.isEmpty(dataBean.getData().getRoomId())) {
            ToastUtils.show(getView().getString(R.string.room_dose_not_exist));
            return;
        }
        if (isChecked) {
            addMemberToRoom(buttonView, new String[]{dataBean.getData().getRoomId()});
        } else {
            deleteMemberFromRoom(buttonView, dataBean.getData().getRoomId());
        }
    }

    @OnClick(R.id.to_admin_user)
    public void onClick(View view) {
        transferAuthority();
    }

    private void transferAuthority() {
        RxUtils.unsubscribeIfNotNull(mSubs4);
//        addSubscription(
//                mSubs4 = mModel.transferAuthority().subscribe());


        //成功的话,直接把管理员字段存下来，因为在开启TabHomeActivity的时候，侧边栏自己会去查找这个字段的值.
        //关闭当前页面，跳转到TabHomeActivity去

        //失败的话就保留在原位置不动.
    }

    private void deleteMemberFromRoom(CompoundButton buttonView, String room_id) {
        getView().showLoading(false);
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.deleteMemberFromRoom(room_id)
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                                revertView(buttonView, true);
                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (trueEntity == null || trueEntity.getData() == null
                                        || TextUtils.isEmpty(trueEntity.getData().getResult())
                                        || !"true".equals(trueEntity.getData().getResult())) {
                                    ToastUtils.show(getView().getString(R.string.delete_member_failed));
                                    revertView(buttonView, true);
                                    return;
                                }
                                ToastUtils.show(getView().getString(R.string.delete_member_success));
                            }
                        }));
    }

    private void addMemberToRoom(CompoundButton buttonView, String[] room_ids) {
        getView().showLoading(false);
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.addMemberToRoom(room_ids)
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                                revertView(buttonView, false);
                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (trueEntity == null || trueEntity.getData() == null
                                        || TextUtils.isEmpty(trueEntity.getData().getResult())
                                        || !"true".equals(trueEntity.getData().getResult())) {
                                    ToastUtils.show(getView().getString(R.string.add_member_failed));
                                    revertView(buttonView, false);
                                    return;
                                }
                                ToastUtils.show(getView().getString(R.string.add_member_success));
                            }
                        }));
    }

    private void revertView(CompoundButton buttonView, boolean checked) {
        if (buttonView != null) {
            buttonView.setChecked(checked);
        }
    }

}
