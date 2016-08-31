package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.HomeMenuItemData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.HomeMenuItemBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeMenuFragmentPresenter;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeMenuFragmentModel extends BaseModel<HomeMenuFragmentPresenter> {

    public HomeMenuFragmentModel(HomeMenuFragmentPresenter presenter) {
        super(presenter);
    }

    public List<DisplayBean> getDatas() {
        List<DisplayBean> displayBeen = new ArrayList<>();

        //细长分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        //短分割线(先声明初始化出来,方便后面使用)
        PropertyPlacardDividerData propertyPlacardDividerData = new PropertyPlacardDividerData();
        propertyPlacardDividerData.setMarginLeft(RuleUtils.dp2Px(14));//和布局中文字的marginLeft相同
        PropertyPlacardDividerBean propertyPlacardDividerBean = new PropertyPlacardDividerBean(propertyPlacardDividerData);

//        String rightText = (TextUtils.isEmpty(UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_NAME))
//                ? UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_NAME)
//                : UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_NAME));
        String rightText = UserManager.INSTANCE.getCurrentTitle();
        for (int i = 0; i < 3; i++) {
            String title = i == 0 ? Utils.appContext.getString(R.string.my_village) : (i == 1 ? Utils.appContext.getString(R.string.polyhome_setting) : Utils.appContext.getString(R.string.message_center));
            //item
            HomeMenuItemData homeMenuItemData = new HomeMenuItemData(title);
            homeMenuItemData.setRightText(i == 0 ? rightText : null);
            HomeMenuItemBean homeMenuItemBean = new HomeMenuItemBean(homeMenuItemData);
            displayBeen.add(homeMenuItemBean);

            if (i == 2) {
                //细长分割线
                displayBeen.add(propertyPlacardDividerLongBean);
            } else {
                //短一些分割线
                displayBeen.add(propertyPlacardDividerBean);
            }
        }

        //大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        propertyRepairBigDividerData.setColor(R.color.color_eaf1f5);
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        displayBeen.add(propertyRepairBigDividerBean);

        //细长分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        //item
        HomeMenuItemData homeMenuItemData1 = new HomeMenuItemData(Utils.appContext.getString(R.string.my_points));
        HomeMenuItemBean homeMenuItemBean1 = new HomeMenuItemBean(homeMenuItemData1);
        displayBeen.add(homeMenuItemBean1);

        //细的分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        //大分割线
        displayBeen.add(propertyRepairBigDividerBean);

        //细的分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        //item
        HomeMenuItemData homeMenuItemData3 = new HomeMenuItemData(Utils.appContext.getString(R.string.settings));
        HomeMenuItemBean homeMenuItemBean3 = new HomeMenuItemBean(homeMenuItemData3);
        displayBeen.add(homeMenuItemBean3);

        //细长分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        return displayBeen;
    }
}
