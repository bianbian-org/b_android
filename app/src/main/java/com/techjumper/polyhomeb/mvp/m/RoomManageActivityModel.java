package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.adapter.recycler_Data.BlackViewData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRoomData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.RoomManageData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.BlackViewBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRoomBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.RoomManageBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.C_AllRoomEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.p.activity.RoomManageActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RoomManageActivityModel extends BaseModel<RoomManageActivityPresenter> {

    public RoomManageActivityModel(RoomManageActivityPresenter presenter) {
        super(presenter);
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

    public Observable<TrueEntity> deleteRoom(String room_id) {
        KeyValuePair keyValuePair = KeyValueCreator.deleteRoom(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , room_id);
        BaseArgumentsEntity baseArguments = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .deleteRoom(baseArguments)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> processDatas(C_AllRoomEntity c_allRoomEntity) {
        List<DisplayBean> displayBeen = new ArrayList<>();

        //空白View
        BlackViewData data = new BlackViewData();
        BlackViewBean bean = new BlackViewBean(data);
        displayBeen.add(bean);

        //细长分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        //短分割线
        PropertyPlacardDividerData propertyPlacardDividerData = new PropertyPlacardDividerData();
        propertyPlacardDividerData.setMarginLeft(RuleUtils.dp2Px(14));//和布局中文字的marginLeft相同
        PropertyPlacardDividerBean propertyPlacardDividerBean = new PropertyPlacardDividerBean(propertyPlacardDividerData);

        //大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);

        List<C_AllRoomEntity.DataEntity.ResultEntity> result = c_allRoomEntity.getData().getResult();

        if (result.size() == 1) {
            C_AllRoomEntity.DataEntity.ResultEntity resultEntity = result.get(0);
            RoomManageData roomManageData = new RoomManageData();
            roomManageData.setRoom_id(resultEntity.getRoom_id());
            roomManageData.setRoom_name(resultEntity.getRoom_name());
            roomManageData.setDeleteMode(getPresenter().isEditMode());
            RoomManageBean roomManageBean = new RoomManageBean(roomManageData);
            displayBeen.add(roomManageBean);

            displayBeen.add(propertyPlacardDividerLongBean);
            displayBeen.add(propertyRepairBigDividerBean);
            displayBeen.add(propertyPlacardDividerLongBean);

            //新增房间
            NewRoomData newRoomData = new NewRoomData();
            newRoomData.setCanShow(getPresenter().isEditMode());
            NewRoomBean newRoomBean = new NewRoomBean(newRoomData);
            displayBeen.add(newRoomBean);
            displayBeen.add(propertyPlacardDividerLongBean);

        } else {
            for (int i = 0; i < result.size(); i++) {
                C_AllRoomEntity.DataEntity.ResultEntity resultEntity = result.get(i);

                RoomManageData roomManageData = new RoomManageData();
                roomManageData.setRoom_id(resultEntity.getRoom_id());
                roomManageData.setRoom_name(resultEntity.getRoom_name());
                roomManageData.setDeleteMode(getPresenter().isEditMode());
                RoomManageBean roomManageBean = new RoomManageBean(roomManageData);
                displayBeen.add(roomManageBean);

                if (i == result.size() - 1) {  //最后一个item
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    displayBeen.add(propertyPlacardDividerBean);
                }
            }
        }

        displayBeen.add(propertyRepairBigDividerBean);
        //新增房间
        NewRoomData newRoomData = new NewRoomData();
        newRoomData.setCanShow(!getPresenter().isEditMode());
        NewRoomBean newRoomBean = new NewRoomBean(newRoomData);
        displayBeen.add(newRoomBean);

        return displayBeen;
    }

    public List<DisplayBean> noData() {
        List<DisplayBean> displayBeen = new ArrayList<>();

        BlackViewData data = new BlackViewData();
        BlackViewBean bean = new BlackViewBean(data);
        displayBeen.add(bean);

        //新增房间
        NewRoomData newRoomData = new NewRoomData();
        newRoomData.setCanShow(!getPresenter().isEditMode());
        NewRoomBean newRoomBean = new NewRoomBean(newRoomData);
        displayBeen.add(newRoomBean);

        return displayBeen;
    }

}
