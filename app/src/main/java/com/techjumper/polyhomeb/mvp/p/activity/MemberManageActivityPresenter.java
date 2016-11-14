package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MemberManageData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberManageBean;
import com.techjumper.polyhomeb.entity.C_AllMemberEntity;
import com.techjumper.polyhomeb.entity.C_RoomsByMemberEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.event.RequestRoomsAndMembersDataEvent;
import com.techjumper.polyhomeb.mvp.m.MemberManageActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MemberDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MemberManageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MemberManageActivityPresenter extends AppBaseActivityPresenter<MemberManageActivity> {

    private Subscription mSubs1, mSubs2, mSubs3, mSubs4;
    private boolean mHasMember = true;
    private boolean mIsEditMode = false;

    public static final String KEY_MEMBER_ID = "key_member_id";
    public static final String KEY_MEMBER_NAME = "key_member_name";
    public static final String KEY_MEMBER_ROOMS = "key_member_rooms";

    /**
     * 保存当前家庭下已有的所有成员数据
     */
    private List<C_AllMemberEntity.DataEntity.UsersEntity> mMembers = new ArrayList<>();
    /**
     * 保存成员id和他对应的房间信息
     */
    private Map<String, C_RoomsByMemberEntity> mAllDataMap = new HashMap<>();

    private MemberManageActivityModel mModel = new MemberManageActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        getMembersAndTheirRooms();
        getRxMessages();
    }

    private void getRxMessages() {
        addSubscription(
                mSubs4 = RxBus.INSTANCE.asObservable()
                        .subscribe(o -> {
                            if (o instanceof RequestRoomsAndMembersDataEvent) {
                                getMembersAndTheirRooms();
                            }
                        }));
    }

    public void onTitleRightClick() {
        if (!mHasMember || getView().getAdapter() == null) return;
        List<DisplayBean> data = getView().getAdapter().getData();
        for (int i = 0; i < data.size(); i++) {
            DisplayBean bean = data.get(i);
            if (bean instanceof MemberManageBean) {
                MemberManageBean manageBean = (MemberManageBean) bean;
                MemberManageData manageData = manageBean.getData();
                manageData.setDeleteMode(!mIsEditMode);
            }
            getView().getAdapter().notifyDataSetChanged();
        }
        mIsEditMode = !mIsEditMode;
    }

    public void onMemberItemClick(MemberManageBean data) {
        MemberManageData data1 = data.getData();
        String memberId = data1.getMemberId();
        String memberName = data1.getMemberName();
        C_RoomsByMemberEntity entity = data1.getEntity();
        if (mIsEditMode) {
            showConfirmDialog(memberId, memberName);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_MEMBER_ID, memberId);
            bundle.putString(KEY_MEMBER_NAME, memberName);
            bundle.putString(KEY_MEMBER_ROOMS, GsonUtils.toJson(entity));
            new AcHelper.Builder(getView()).extra(bundle).target(MemberDetailActivity.class).start();
        }
    }

    /**
     * 首先查询家庭内所有成员，然后根据每位成员id，查询其名下的所有房间
     */
    private void getMembersAndTheirRooms() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getAllMember()
                        .flatMap(new Func1<C_AllMemberEntity, Observable<C_AllMemberEntity.DataEntity.UsersEntity>>() {
                            @Override
                            public Observable<C_AllMemberEntity.DataEntity.UsersEntity> call(C_AllMemberEntity c_allMemberEntity) {
                                if (!processNetworkResult(c_allMemberEntity)) {
                                    getView().onMembersAndRoomsDataReceive(mModel.noData());
                                    return Observable.error(new Exception("数据错误"));
                                }
                                if (c_allMemberEntity == null
                                        || c_allMemberEntity.getData() == null
                                        || c_allMemberEntity.getData().getUsers() == null) {
                                    getView().onMembersAndRoomsDataReceive(mModel.noData());
                                    return Observable.error(new Exception("数据错误"));
                                }
                                if (c_allMemberEntity.getData().getUsers().size() == 0) {
                                    getView().onMembersAndRoomsDataReceive(mModel.noData());
                                    return Observable.error(new Exception("暂无任何成员"));
                                }
                                saveMemberInfos(c_allMemberEntity.getData().getUsers());
                                return Observable.from(c_allMemberEntity.getData().getUsers());
                            }
                        })
                        .concatMap(new Func1<C_AllMemberEntity.DataEntity.UsersEntity, Observable<C_RoomsByMemberEntity>>() {
                            @Override
                            public Observable<C_RoomsByMemberEntity> call(C_AllMemberEntity.DataEntity.UsersEntity usersEntity) {
                                getRooms(String.valueOf(usersEntity.getId()));
                                return Observable.empty();
                            }
                        })
                        .subscribe(new Observer<C_RoomsByMemberEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showHint(e.toString());
                                getView().dismissLoading();
                                mHasMember = false;
                                mIsEditMode = false;
                            }

                            @Override
                            public void onNext(C_RoomsByMemberEntity c_roomsByMemberEntity) {
                            }
                        }));
    }

    /**
     * 通过成员id查名下所有房间
     */
    private void getRooms(String id) {
        addSubscription(
                mSubs2 = mModel.getRoomsByMember(id)
                        .subscribe(new Observer<C_RoomsByMemberEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                                mHasMember = false;
                                mIsEditMode = false;
                            }

                            @Override
                            public void onNext(C_RoomsByMemberEntity c_roomsByMemberEntity) {
                                if (!processNetworkResult(c_roomsByMemberEntity)) {
                                    mAllDataMap.put(id, null);
                                    updateView();
                                    return;
                                }
                                if (c_roomsByMemberEntity.getData() == null
                                        || c_roomsByMemberEntity.getData().getResult() == null
                                        || c_roomsByMemberEntity.getData().getResult().size() == 0) {
                                    mAllDataMap.put(id, null);
                                    updateView();
                                    return;
                                }
                                mAllDataMap.put(id, c_roomsByMemberEntity);
                                updateView();
                            }
                        }));
    }

    /**
     * 保存成员数据
     */
    private void saveMemberInfos(List<C_AllMemberEntity.DataEntity.UsersEntity> users) {
        mAllDataMap.clear();
        mMembers.clear();
        mMembers.addAll(users);//保存所有成员数据
    }

    /**
     * 判断当前数据状态，
     */
    private void updateView() {
        mHasMember = true;
        mIsEditMode = false;
        //相等的话，说明是最后一条数据了
        if (mMembers.size() == mAllDataMap.size()) {
            getView().onMembersAndRoomsDataReceive(mModel.processData(mAllDataMap));
            getView().dismissLoading();
        }
    }

    public List<C_AllMemberEntity.DataEntity.UsersEntity> getMembers() {
        return mMembers;
    }

    public boolean isEditMode() {
        return mIsEditMode;
    }

    private void showConfirmDialog(String member_id, String member_name) {
        DialogUtils.getBuilder(getView())
                .negativeText(R.string.cancel)
                .positiveText(R.string.ok)
                .content(String.format(getView().getString(R.string.confirm_delete_x_member), member_name))
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            deleteMember(member_id, member_name);
                            break;
                    }
                })
                .show();
    }

    private void deleteMember(String id, String name) {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.deleteMember(id)
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {
                                getMembersAndTheirRooms();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMembersAndTheirRooms();
                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (trueEntity == null
                                        || trueEntity.getData() == null
                                        || TextUtils.isEmpty(trueEntity.getData().getResult())
                                        || !"true".equalsIgnoreCase(trueEntity.getData().getResult())) {
                                    ToastUtils.show(String.format(
                                            getView().getString(R.string.delete_member_x_failed), name));
                                }
                            }
                        }));
    }

}
