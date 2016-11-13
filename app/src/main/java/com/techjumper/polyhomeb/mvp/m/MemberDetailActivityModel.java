package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.adapter.recycler_Data.MemberDetailData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberDetailBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.C_RoomsByMemberEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MemberDetailActivityPresenter;

import java.util.ArrayList;
import java.util.List;

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

    private String getMemberId() {
        return getExtras().getString(KEY_MEMBER_ID, "");
    }

    private C_RoomsByMemberEntity getRooms() {
        return GsonUtils.fromJson(getExtras().getString(KEY_MEMBER_ROOMS, ""), C_RoomsByMemberEntity.class);
//        List<C_RoomsByMemberEntity.DataEntity.ResultEntity> entity = new ArrayList<>();
//        C_RoomsByMemberEntity.DataEntity dataEntity = new C_RoomsByMemberEntity.DataEntity();
//        C_RoomsByMemberEntity c_roomsByMemberEntity = new C_RoomsByMemberEntity();
//
//        for (int i = 0; i < 30; i++) {
//            C_RoomsByMemberEntity.DataEntity.ResultEntity resultEntity = new C_RoomsByMemberEntity.DataEntity.ResultEntity();
//            resultEntity.setRoom_id(i + "");
//            resultEntity.setRoom_name("张三" + i);
//            entity.add(resultEntity);
//        }
//        dataEntity.setResult(entity);
//        c_roomsByMemberEntity.setData(dataEntity);
//        return c_roomsByMemberEntity;
    }

    public List<DisplayBean> loadData() {
        if (getRooms() == null || getRooms().getData() == null || getRooms().getData().getResult() == null
                || getRooms().getData().getResult().size() == 0) {
            return noData();
        } else {
            return showDatas();
        }
    }

    private List<DisplayBean> showDatas() {

        List<DisplayBean> displayBeen = new ArrayList<>();

        //细长分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        //短分割线
        PropertyPlacardDividerData propertyPlacardDividerData = new PropertyPlacardDividerData();
        propertyPlacardDividerData.setMarginLeft(RuleUtils.dp2Px(14));//和布局中文字的marginLeft相同
        PropertyPlacardDividerBean propertyPlacardDividerBean = new PropertyPlacardDividerBean(propertyPlacardDividerData);

        List<C_RoomsByMemberEntity.DataEntity.ResultEntity> result = getRooms().getData().getResult();
        if (result.size() == 1) {
            C_RoomsByMemberEntity.DataEntity.ResultEntity resultEntity = result.get(0);
            MemberDetailData memberDetailData = new MemberDetailData();
            memberDetailData.setRoomName(resultEntity.getRoom_name());
            memberDetailData.setRoomId(resultEntity.getRoom_id());
            memberDetailData.setManageable(1);
            MemberDetailBean memberDetailBean = new MemberDetailBean(memberDetailData);
            displayBeen.add(memberDetailBean);
            displayBeen.add(propertyPlacardDividerLongBean);
        } else {
            for (int i = 0; i < result.size(); i++) {
                C_RoomsByMemberEntity.DataEntity.ResultEntity resultEntity = result.get(i);
                MemberDetailData memberDetailData = new MemberDetailData();
                memberDetailData.setRoomName(resultEntity.getRoom_name());
                memberDetailData.setRoomId(resultEntity.getRoom_id());
                memberDetailData.setManageable(i);
                MemberDetailBean memberDetailBean = new MemberDetailBean(memberDetailData);
                displayBeen.add(memberDetailBean);

                if (i == result.size() - 1) {  //最后一个item
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
