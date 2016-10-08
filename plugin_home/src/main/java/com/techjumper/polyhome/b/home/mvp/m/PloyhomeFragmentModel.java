package com.techjumper.polyhome.b.home.mvp.m;

import android.util.Log;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.HeartbeatEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.util.StringUtil;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.net.KeyValueCreator;
import com.techjumper.polyhome.b.home.net.NetHelper;
import com.techjumper.polyhome.b.home.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/4/28.
 */
public class PloyhomeFragmentModel extends BaseModel<PloyhomeFragmentPresenter> {

    public PloyhomeFragmentModel(PloyhomeFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<InfoEntity> getInfo(int page) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.getInfo(UserInfoManager.getUserId(), UserInfoManager.getTicket(), String.valueOf(page), "10")))
                .compose(CommonWrap.wrap());
    }

    public Observable<NoticeEntity> getNotices() {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getNotices(NetHelper.createBaseArgumentsMap(KeyValueCreator.getNotices(UserInfoManager.getUserId(), UserInfoManager.getFamilyId(), UserInfoManager.getTicket())))
                .compose(CommonWrap.wrap());
    }

    public Observable<HeartbeatEntity> submitOnline() {
        KeyValuePair keyValuePair = KeyValueCreator.submitOnline(UserInfoManager.getFamilyId(), StringUtil.getMacAddress());
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        Log.d("submitOnline", "familyId: " + UserInfoManager.getFamilyId() + "  deviceId: " + StringUtil.getMacAddress());
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitOnline(argument)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> submitTimer(String timer) {
        KeyValuePair keyValuePair = KeyValueCreator.submitTimer(UserInfoManager.getFamilyId(), timer);
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .submitTimer(argument)
                .compose(CommonWrap.wrap());
    }
}
