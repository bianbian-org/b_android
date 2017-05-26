package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.DnakeLockData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.UnlockVillageTextData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.DnakeLockBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.UnlockVillageTextBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.VillageLockEntity;
import com.techjumper.polyhomeb.mvp.p.activity.DnakeActivityPresenter;
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
 * Date: 2016/12/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DnakeActivityModel extends BaseModel<DnakeActivityPresenter> {

    public DnakeActivityModel(DnakeActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<VillageLockEntity> getVillageLocks() {
        KeyValuePair keyValuePair = KeyValueCreator.getVillageLocks(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getVillageLocks(map)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> unlock(int id) {
        KeyValuePair keyValuePair = KeyValueCreator.unLock(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , id);
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .unLock(entity)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> processDatas(VillageLockEntity villageLockEntity) {

        List<DisplayBean> displayBeen = new ArrayList<>();

        //围墙机
        List<VillageLockEntity.DataBean.OutdoorLocksBean> outdoor_locks = villageLockEntity.getData().getOutdoor_locks();
        //门口机
        List<VillageLockEntity.DataBean.UnitLocksBean> unit_locks = villageLockEntity.getData().getUnit_locks();

        if (outdoor_locks.size() != 0) {
            //文字
            UnlockVillageTextData unlockVillageTextData = new UnlockVillageTextData();
            unlockVillageTextData.setText(getPresenter().getView().getString(R.string.out_door));
            UnlockVillageTextBean unlockVillageTextBean = new UnlockVillageTextBean(unlockVillageTextData);
            displayBeen.add(unlockVillageTextBean);

            for (int i = 0; i < outdoor_locks.size(); i++) {
                if (i == 0) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
                DnakeLockData dnakeLockData = new DnakeLockData();
                dnakeLockData.setName(outdoor_locks.get(i).getLock_name());
                dnakeLockData.setId(outdoor_locks.get(i).getLock_id());
                DnakeLockBean dnakeLockBean = new DnakeLockBean(dnakeLockData);
                displayBeen.add(dnakeLockBean);
                if (i == outdoor_locks.size() - 1) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
            }
        }

        if (unit_locks.size() != 0) {
            //文字
            UnlockVillageTextData unlockVillageTextData = new UnlockVillageTextData();
            unlockVillageTextData.setText(getPresenter().getView().getString(R.string.unit_door));
            UnlockVillageTextBean unlockVillageTextBean = new UnlockVillageTextBean(unlockVillageTextData);
            displayBeen.add(unlockVillageTextBean);

            for (int i = 0; i < unit_locks.size(); i++) {
                if (i == 0) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
                DnakeLockData dnakeLockData = new DnakeLockData();
                dnakeLockData.setName(unit_locks.get(i).getLock_name());
                dnakeLockData.setId(unit_locks.get(i).getLock_id());
                DnakeLockBean dnakeLockBean = new DnakeLockBean(dnakeLockData);
                displayBeen.add(dnakeLockBean);

                if (i == unit_locks.size() - 1) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
            }
        }
        return displayBeen;
    }
}
