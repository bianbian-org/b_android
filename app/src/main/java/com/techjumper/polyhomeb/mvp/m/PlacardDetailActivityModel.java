package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;
import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.NoticeDetailEntity;
import com.techjumper.polyhomeb.mvp.p.activity.PlacardDetailActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardDetailActivityModel extends BaseModel<PlacardDetailActivityPresenter> {

    public PlacardDetailActivityModel(PlacardDetailActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getTitle() {
        return getExtras().getString(Constant.PLACARD_DETAIL_TITLE, "");
    }

    public String getContent() {
        return getExtras().getString(Constant.PLACARD_DETAIL_CONTENT, "");
    }

    public String getType() {
        return getExtras().getString(Constant.PLACARD_DETAIL_TYPE, "");
    }

    public String getTime() {
        String temp = getExtras().getString(Constant.PLACARD_DETAIL_TIME, "");
        if (TextUtils.isEmpty(temp)) return "";
        return temp;
    }

    public int getId() {
        return getExtras().getInt(Constant.PLACARD_DETAIL_ID, 1);
    }

    public String getComeFrom() {
        return getExtras().getString(Constant.PLACARD_DETAIL_COME_FROM, "");
    }

    public Observable<NoticeDetailEntity> getDetail() {
        KeyValuePair keyValuePair = KeyValueCreator.getNoticeDetail(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getId() + "");
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getNoticeDetail(map)
                .compose(CommonWrap.wrap());
    }
}
