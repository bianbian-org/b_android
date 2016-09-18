package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.MedicalChangeAccountEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangeAccountActivityPresenter;
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
public class MedicalChangeAccountActivityModel extends BaseModel<MedicalChangeAccountActivityPresenter> {

    public MedicalChangeAccountActivityModel(MedicalChangeAccountActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<MedicalChangeAccountEntity> getMedicalUserListData() {

        KeyValuePair keyValuePair = KeyValueCreator.getMedicalUserData(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket());
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getMedicalUserData(entity)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> getData(MedicalChangeAccountEntity entity) {
        List<DisplayBean> displayBeen = new ArrayList<>();


        return displayBeen;
    }

    public List<DisplayBean> getEmptyData() {
        List<DisplayBean> displayBeen = new ArrayList<>();


        return displayBeen;
    }

}
