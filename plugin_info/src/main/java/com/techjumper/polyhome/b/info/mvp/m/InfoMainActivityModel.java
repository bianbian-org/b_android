package com.techjumper.polyhome.b.info.mvp.m;

import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.polyhome.b.info.Config;
import com.techjumper.polyhome.b.info.mvp.p.activity.InfoMainActivityPresenter;
import com.techjumper.polyhome.b.info.net.KeyValueCreator;
import com.techjumper.polyhome.b.info.net.NetHelper;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhome.b.info.net.ServiceAPI;

import rx.Observable;

/**
 * Created by kevin on 16/5/4.
 */
public class InfoMainActivityModel extends BaseModel<InfoMainActivityPresenter> {

    public InfoMainActivityModel(InfoMainActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<AnnouncementEntity> getAnnouncements(int page) {
        return RetrofitHelper.<ServiceAPI>create(Config.sBaseBUrl, ServiceAPI.class)
                .getAnnouncements(NetHelper.createBaseArgumentsMap(KeyValueCreator.getAnnouncements(String.valueOf(page), ComConstant.PAGESIZE)))
                .compose(CommonWrap.wrap());
    }

    public Observable<InfoEntity> getInfo(int page) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.getInfo(ComConstant.defaultUserId, ComConstant.defaultTicket, String.valueOf(page), ComConstant.PAGESIZE)))
                .compose(CommonWrap.wrap());
    }

    public Observable<InfoEntity> getInfo(int type, int page) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getInfo(NetHelper.createBaseArgumentsMap(KeyValueCreator.getInfo(ComConstant.defaultUserId, ComConstant.defaultTicket, String.valueOf(type), String.valueOf(page), ComConstant.PAGESIZE)))
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> readMessage(long message_id) {
        KeyValuePair loginPair = KeyValueCreator.readMessage(ComConstant.defaultUserId, ComConstant.defaultTicket, String.valueOf(message_id));
        BaseArgumentsEntity argument = NetHelper.createBaseArguments(loginPair);

        return RetrofitHelper.<ServiceAPI>createDefault()
                .readMessage(argument)
                .compose(CommonWrap.wrap());
    }
}
