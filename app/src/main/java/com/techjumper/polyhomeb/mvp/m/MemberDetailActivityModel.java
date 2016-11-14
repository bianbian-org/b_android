package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.adapter.recycler_Data.MemberDetailData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberDetailBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.C_AllRoomEntity;
import com.techjumper.polyhomeb.entity.C_RoomsByMemberEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MemberDetailActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.techjumper.polyhomeb.mvp.p.activity.MemberManageActivityPresenter.KEY_MEMBER_ID;
import static com.techjumper.polyhomeb.mvp.p.activity.MemberManageActivityPresenter.KEY_MEMBER_NAME;
import static com.techjumper.polyhomeb.mvp.p.activity.MemberManageActivityPresenter.KEY_MEMBER_ROOMS;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MemberDetailActivityModel extends BaseModel<MemberDetailActivityPresenter> {

    public MemberDetailActivityModel(MemberDetailActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getMemberName() {
        return getExtras().getString(KEY_MEMBER_NAME, "");
    }

    public String getMemberId() {
        return getExtras().getString(KEY_MEMBER_ID, "");
    }

    public Observable<C_AllRoomEntity> getAllRooms() {
        KeyValuePair keyValuePair = KeyValueCreator.getAllRooms(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID)
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));
        BaseArgumentsEntity baseArguments = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .getAllRooms(baseArguments)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> deleteMemberFromRoom(String delete_room_id) {
        KeyValuePair keyValuePair = KeyValueCreator.deleteMemberFromRoom(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , delete_room_id
                , getMemberId());
        BaseArgumentsEntity baseArgumentsEntity = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .deleteMemberFromRoom(baseArgumentsEntity)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> addMemberToRoom(String[] room_ids) {
        KeyValuePair keyValuePair = KeyValueCreator.addMemberToRoom(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getMemberId()
                , room_ids);
        BaseArgumentsEntity baseArguments = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .addMemberToRoom(baseArguments)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> transferAuthority() {
        KeyValuePair keyValuePair = KeyValueCreator.transferAuthority(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID)
                , getMemberId());
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .transferAuthority(entity)
                .compose(CommonWrap.wrap());
    }

    private C_RoomsByMemberEntity getRooms() {
        C_RoomsByMemberEntity c_roomsByMemberEntity = GsonUtils.fromJson(getExtras().getString(KEY_MEMBER_ROOMS, ""), C_RoomsByMemberEntity.class);
        return c_roomsByMemberEntity == null || c_roomsByMemberEntity.getData() == null
                || c_roomsByMemberEntity.getData().getResult() == null
                || c_roomsByMemberEntity.getData().getResult().size() == 0 ? null : c_roomsByMemberEntity;
    }

    public List<DisplayBean> loadData(C_AllRoomEntity c_allRoomEntity) {
        return c_allRoomEntity == null ? noData() : showDatas(c_allRoomEntity);
    }

    private List<DisplayBean> showDatas(C_AllRoomEntity c_allRoomEntity) {

        List<DisplayBean> displayBeen = new ArrayList<>();

        //细长分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        //短分割线
        PropertyPlacardDividerData propertyPlacardDividerData = new PropertyPlacardDividerData();
        propertyPlacardDividerData.setMarginLeft(RuleUtils.dp2Px(14));//和布局中文字的marginLeft相同
        PropertyPlacardDividerBean propertyPlacardDividerBean = new PropertyPlacardDividerBean(propertyPlacardDividerData);

        List<C_AllRoomEntity.DataEntity.ResultEntity> rooms = c_allRoomEntity.getData().getResult();

        if (rooms.size() == 1) {
            boolean manageable = false;
            if (getRooms() != null) {
                List<C_RoomsByMemberEntity.DataEntity.ResultEntity> result = getRooms().getData().getResult();
                for (int i = 0; i < result.size(); i++) {
                    C_RoomsByMemberEntity.DataEntity.ResultEntity resultEntity = result.get(i);
                    String room_id = resultEntity.getRoom_id();
                    if (rooms.get(0).getRoom_id().equalsIgnoreCase(room_id)) {
                        manageable = true;
                        break;
                    } else {
                        manageable = false;
                    }
                }
            }
            C_AllRoomEntity.DataEntity.ResultEntity resultEntity = rooms.get(0);
            MemberDetailData memberDetailData = new MemberDetailData();
            memberDetailData.setRoomName(resultEntity.getRoom_name());
            memberDetailData.setRoomId(resultEntity.getRoom_id());
            memberDetailData.setManageable(manageable);
            MemberDetailBean memberDetailBean = new MemberDetailBean(memberDetailData);
            displayBeen.add(memberDetailBean);
            displayBeen.add(propertyPlacardDividerLongBean);
        } else {
            for (int i = 0; i < rooms.size(); i++) {
                boolean manageable = false;
                if (getRooms() != null) {
                    List<C_RoomsByMemberEntity.DataEntity.ResultEntity> result = getRooms().getData().getResult();
                    for (int j = 0; j < result.size(); j++) {
                        String room_id = result.get(j).getRoom_id();
                        if (rooms.get(i).getRoom_id().equalsIgnoreCase(room_id)) {
                            manageable = true;
                            break;
                        } else {
                            manageable = false;
                        }
                    }
                }
                C_AllRoomEntity.DataEntity.ResultEntity resultEntity = rooms.get(i);
                MemberDetailData memberDetailData = new MemberDetailData();
                memberDetailData.setRoomName(resultEntity.getRoom_name());
                memberDetailData.setRoomId(resultEntity.getRoom_id());
                memberDetailData.setManageable(manageable);
                MemberDetailBean memberDetailBean = new MemberDetailBean(memberDetailData);
                displayBeen.add(memberDetailBean);

                if (i == rooms.size() - 1) {  //最后一个item
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    displayBeen.add(propertyPlacardDividerBean);
                }
            }
        }
        return displayBeen;
    }

    private List<DisplayBean> noData() {
        List<DisplayBean> displayBeen = new ArrayList<>();
        NoDataData noDataData = new NoDataData();
        NoDataBean noDataBean = new NoDataBean(noDataData);
        displayBeen.add(noDataBean);
        return displayBeen;
    }
}
