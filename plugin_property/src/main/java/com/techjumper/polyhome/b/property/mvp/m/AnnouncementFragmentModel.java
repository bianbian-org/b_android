package com.techjumper.polyhome.b.property.mvp.m;

import android.util.Log;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.util.StringUtil;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.p.fragment.AnnouncementPresenter;
import com.techjumper.polyhome.b.property.net.KeyValueCreator;
import com.techjumper.polyhome.b.property.net.NetHelper;
import com.techjumper.polyhome.b.property.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/10/27.
 */

public class AnnouncementFragmentModel extends BaseModel<AnnouncementPresenter> {

    public AnnouncementFragmentModel(AnnouncementPresenter presenter) {
        super(presenter);
    }

    public Observable<AnnouncementEntity> getAnnouncements(int page) {

        return RetrofitHelper.<ServiceAPI>createDefault()
                .getAnnouncements(NetHelper.createBaseArgumentsMap(KeyValueCreator.getAnnouncements(UserInfoManager.getUserId(), UserInfoManager.getFamilyId(), UserInfoManager.getTicket(), String.valueOf(page), ComConstant.PAGESIZE)))
                .compose(CommonWrap.wrap());
    }

    public Observable<HeartbeatEntity> submitOnline() {
        KeyValuePair keyValuePair = KeyValueCreator.submitOnline(UserInfoManager.getTicket(), StringUtil.getMacAddress());
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        Log.d("submitOnline", "familyId: " + UserInfoManager.getFamilyId() + "  deviceId: " + StringUtil.getMacAddress());
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitOnline(argument)
                .compose(CommonWrap.wrap());
    }
}
