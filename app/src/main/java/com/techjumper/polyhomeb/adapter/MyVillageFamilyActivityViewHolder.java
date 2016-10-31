package com.techjumper.polyhomeb.adapter;

import android.view.View;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MyVillageFamilyData;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MyVillageFamilyData.class, beanName = "MyVillageFamilyBean")
public class MyVillageFamilyActivityViewHolder extends BaseRecyclerViewHolder<MyVillageFamilyData> {

    public static final int LAYOUT_ID = R.layout.item_my_village_family;

    public MyVillageFamilyActivityViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MyVillageFamilyData data) {
        if (data == null) return;
        int villageId = data.getVillageId();
        int family_id = data.getFamily_id();   //家庭or小区id
        String name = data.getName();  //家庭or小区名字
        if (data.isFamilyData() == 0) { //家庭
            setText(R.id.tv_name, data.getName());
        } else if (data.isFamilyData() == 1) { //小区
            boolean isNoRoomVerified = false;
            List<UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean.RoomsBean> rooms = data.getRooms();
            StringBuffer verifiedRoom = new StringBuffer();
            verifiedRoom.append("(");
            for (int i = 0; i < rooms.size(); i++) {
                UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean.RoomsBean roomsBean = rooms.get(i);
                String room_num = roomsBean.getRoom_num();
                int verified = roomsBean.getVerified();  //0是未审核,1是已审核
                if (1 == verified) {
                    verifiedRoom.append(room_num + "、");
                    isNoRoomVerified = true;
                }
            }
            verifiedRoom.append(")");
            verifiedRoom.deleteCharAt(verifiedRoom.length() - 2);
            if (isNoRoomVerified) {
                setText(R.id.tv_name, data.getName() + verifiedRoom.toString());
            } else {
                setText(R.id.tv_name, data.getName() + getContext().getString(R.string.uncheck));
            }
        }
        if (data.isChoosed()) {
            PicassoHelper.load(R.mipmap.icon_choose_green).into((ImageView) getView(R.id.iv_choose));
        } else {
            setImageBitmap(R.id.iv_choose, null);
        }
        setOnClickListener(R.id.layout_village, v -> {
            // HomeFragment和HomeMenuFragment中收到消息,和MyVillageFamilyActivityPresenter中收到消息是同时的,
            // 会导致那时候SP中还没存数据,但是调用和更新title和侧边栏的方法,导致界面没更新.
            if (!data.isChoosed()) {
                if (0 == data.isFamilyData()) {  //家庭
                    UserManager.INSTANCE.updateFamilyOrVillageInfo(true, family_id + "", name, villageId + "");
                } else if (1 == data.isFamilyData()) {   //小区
                    UserManager.INSTANCE.updateFamilyOrVillageInfo(false, villageId + "", name, villageId + "");
                }
                RxBus.INSTANCE.send(new ChooseFamilyVillageEvent(name, data.isFamilyData(), getLayoutPosition()));
            }
        });
    }
}
