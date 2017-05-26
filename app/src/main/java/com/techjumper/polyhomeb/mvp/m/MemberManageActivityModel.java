package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.adapter.recycler_Data.BlackViewData;
import com.techjumper.polyhomeb.adapter.recycler_Data.MemberManageData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.BlackViewBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberManageBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.C_AllMemberEntity;
import com.techjumper.polyhomeb.entity.C_RoomsByMemberEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MemberManageActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MemberManageActivityModel extends BaseModel<MemberManageActivityPresenter> {

    public MemberManageActivityModel(MemberManageActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<C_AllMemberEntity> getAllMember() {
        KeyValuePair keyValuePair = KeyValueCreator.getAllMember(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID));
        BaseArgumentsEntity baseArguments = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .getAllMember(baseArguments)
                .compose(CommonWrap.wrap());
    }

    public Observable<C_RoomsByMemberEntity> getRoomsByMember(String query_user_id) {
        KeyValuePair keyValuePair = KeyValueCreator.getRoomsByMember(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID)
                , query_user_id);
        BaseArgumentsEntity baseArguments = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .getRoomsByMember(baseArguments)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> deleteMember(String delete_user_id) {
        KeyValuePair keyValuePair = KeyValueCreator.deleteMember(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID)
                , delete_user_id);
        BaseArgumentsEntity baseArgumentsEntity = NetHelper.createBaseCAPPArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createCAPPDefault()
                .deleteMember(baseArgumentsEntity)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> processData(Map<String, C_RoomsByMemberEntity> allDataMap) {
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

        List<C_AllMemberEntity.DataEntity.UsersEntity> members = getPresenter().getMembers();

        if (allDataMap.size() == 1) {
            String id = "";
            String name = "";
            if (members != null && members.size() != 0) {
                C_AllMemberEntity.DataEntity.UsersEntity usersEntity = members.get(0);
                id = usersEntity.getId() + "";
                name = usersEntity.getUsername();
            }
            C_RoomsByMemberEntity entity = null;
            for (C_RoomsByMemberEntity value : allDataMap.values()) {
                entity = value;
            }
            MemberManageData memberManageData = new MemberManageData();
            memberManageData.setEntity(entity);
            memberManageData.setMemberId(id);
            memberManageData.setMemberName(name);
            memberManageData.setDeleteMode(getPresenter().isEditMode());
            MemberManageBean memberManageBean = new MemberManageBean(memberManageData);
            displayBeen.add(memberManageBean);

            displayBeen.add(propertyPlacardDividerLongBean);

        } else {
            for (int i = 0; i < allDataMap.size(); i++) {
                C_AllMemberEntity.DataEntity.UsersEntity usersEntity = members.get(i);
                int id = usersEntity.getId();
                String username = usersEntity.getUsername();
                C_RoomsByMemberEntity entity = allDataMap.get(id + "");
                MemberManageData memberManageData = new MemberManageData();
                memberManageData.setEntity(entity);
                memberManageData.setMemberId(id + "");
                memberManageData.setMemberName(username);
                memberManageData.setDeleteMode(getPresenter().isEditMode());
                MemberManageBean memberManageBean = new MemberManageBean(memberManageData);
                displayBeen.add(memberManageBean);
                if (i == allDataMap.size() - 1) {  //最后一个item
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    displayBeen.add(propertyPlacardDividerBean);
                }
            }
        }

        return displayBeen;
    }

    /**
     * 无数据的时候显示的视图
     */
    public List<DisplayBean> noData() {
        List<DisplayBean> displayBeen = new ArrayList<>();
        NoDataData noDataData = new NoDataData();
        NoDataBean noDataBean = new NoDataBean(noDataData);
        displayBeen.add(noDataBean);
        return displayBeen;
    }
}
