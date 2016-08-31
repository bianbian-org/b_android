package com.techjumper.polyhomeb.adapter;

import android.view.View;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.file.PreferenceUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MyVillageFamilyData;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.user.UserManager;

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
        int id = data.getId();   //家庭or小区id
        String name = data.getName();  //家庭or小区名字
        int verified = data.getVerified();  //0是未审核,1是已审核
        if (data.isFamilyData() == 0) { //家庭
            setText(R.id.tv_name, data.getName());
        } else if (data.isFamilyData() == 1) { //小区
            setText(R.id.tv_name, data.getName() + (verified == 0 ? getContext().getString(R.string.uncheck) : getContext().getString(R.string.check)));
        }
        if (data.isChoosed()) {
            PicassoHelper.load(R.mipmap.icon_choose_green).into((ImageView) getView(R.id.iv_choose));
        } else {
            setImageBitmap(R.id.iv_choose, null);
        }
        setOnClickListener(R.id.layout_village, v -> {
            //下面这个if else必须在点击的时候做,不然..
            // HomeFragment和HomeMenuFragment中收到消息,和MyVillageFamilyActivityPresenter中收到消息是同时的,
            // 会导致那时候SP中还没存数据,但是调用和更新title和侧边栏的方法,导致界面没更新.
            //(最开始是写在MyVillageFamilyActivityPresenter中的,肯定不得行)
            if (0 == data.isFamilyData()) {  //家庭
                PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_ID, id + "");
                PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_NAME, name);
                PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, UserManager.VALUE_IS_FAMILY);
            } else if (1 == data.isFamilyData()) {   //小区
                PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_ID, id + "");
                PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_TITLE_NAME, name);
                PreferenceUtils.save(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE, UserManager.VALUE_IS_VILLAGE);
            }
            RxBus.INSTANCE.send(new ChooseFamilyVillageEvent(id, name, data.isFamilyData(), getLayoutPosition()));
        });
    }
}
