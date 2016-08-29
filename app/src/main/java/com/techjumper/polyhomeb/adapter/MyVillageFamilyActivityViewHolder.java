package com.techjumper.polyhomeb.adapter;

import android.view.View;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MyVillageFamilyData;
import com.techjumper.polyhomeb.entity.event.ChooseVillageFamilyEvent;
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
        if (data.isFamilyData()) {
            setText(R.id.tv_name, data.getName());
        } else {
            setText(R.id.tv_name, data.getName() + (verified == 0 ? getContext().getString(R.string.uncheck) : getContext().getString(R.string.check)));
        }
        if (data.isChoosed()) {
            PicassoHelper.getDefault().load(R.mipmap.icon_choose_green).into((ImageView) getView(R.id.iv_choose));
        } else {
            setImageBitmap(R.id.iv_choose, null);
        }

        setOnClickListener(R.id.layout_village, v -> {
            //如果已经打钩了,不作任何操作,如果没有被打钩,现在点击了整个布局,那么就应该让他打钩,并且发送RxBus,让其他的不打勾.
            if (!data.isChoosed()) {
                if (data.isFamilyData()) {
                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_FAMILY_NAME, name);
                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_FAMILY_ID, id + "");
                } else {
                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_VILLAGE_NAME, name);
                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID, id + "");
                }
                RxBus.INSTANCE.send(new ChooseVillageFamilyEvent(id, name, verified, data.isFamilyData()));
            }
        });
    }
}
