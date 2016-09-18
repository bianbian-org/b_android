package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalMainData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MedicalMainBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.MedicalMainEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalMainActivityPresenter;
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
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainActivityModel extends BaseModel<MedicalMainActivityPresenter> {

    public MedicalMainActivityModel(MedicalMainActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<MedicalMainEntity> getMedicalData() {
        KeyValuePair keyValuePair = KeyValueCreator.getMedicalMainData(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket());
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getMedicalMainData(entity)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> getData(MedicalMainEntity entity) {
        List<DisplayBean> displayBeen = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            MedicalMainData medicalMainData = new MedicalMainData();
//            medicalMainData.setIcon(getIconByPosition(i, entity));
//            medicalMainData.setLabel(getLabelByPosition(i, entity));
//            medicalMainData.setData(getDataByPosition(i, entity));
//            medicalMainData.setUnit(getUnitByPosition(i, entity));

            MedicalMainBean medicalMainBean = new MedicalMainBean(medicalMainData);
            displayBeen.add(medicalMainBean);
        }

        return displayBeen;
    }
}
