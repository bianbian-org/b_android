package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.UserInfoEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.entity.event.NoticeEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.WeatherEvent;
import com.techjumper.commonres.entity.event.pushevent.NoticePushEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.m.PloyhomeFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.JujiaActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.ShoppingActivity;
import com.techjumper.polyhome.b.home.mvp.v.fragment.PloyhomeFragment;
import com.techjumper.polyhome.b.home.utils.DateUtil;
import com.techjumper.polyhome.b.home.utils.StringUtil;
import com.techjumper.polyhome.b.home.widget.SquareView;

import java.util.List;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/4/28.
 */
public class PloyhomeFragmentPresenter extends AppBaseFragmentPresenter<PloyhomeFragment> {

    private PloyhomeFragmentModel model = new PloyhomeFragmentModel(this);
    private int type = -1;
    private UserInfoEntity userInfoEntity;

    @OnClick(R.id.property)
    void property() {
        if (UserInfoManager.isLogin()) {
            long familyId = UserInfoManager.getLongFamilyId();
            long userId = UserInfoManager.getLongUserId();
            String ticket = UserInfoManager.getTicket();
            PluginEngineUtil.startProperty(familyId, userId, ticket);
        } else {
            ToastUtils.show(getView().getString(R.string.error_no_login));
        }
    }

    @OnClick(R.id.notice_layout)
    void noticeLayout() {
        if (type == -1)
            return;

        if (UserInfoManager.isLogin()) {
            long userId = UserInfoManager.getLongUserId();
            long familyId = UserInfoManager.getLongFamilyId();
            String ticket = UserInfoManager.getTicket();
            PluginEngineUtil.startInfo(userId, familyId, ticket, type);
        } else {
            ToastUtils.show(getView().getString(R.string.error_no_login));
        }
    }

    @OnClick(R.id.ad)
    void ad() {
        Intent intent = new Intent(getView().getActivity(), AdActivity.class);
        getView().getActivity().startActivity(intent);
    }

    @OnClick(R.id.shopping)
    void shopping() {
        if (TextUtils.isEmpty(UserInfoManager.getTicket()) || TextUtils.isEmpty(UserInfoManager.getUserId())) {
            ToastUtils.show(getView().getString(R.string.error_no_login));
        } else {
            Intent intent = new Intent(getView().getActivity(), ShoppingActivity.class);
            getView().startActivity(intent);
        }
    }

    @OnClick(R.id.jujia)
    void jujia() {
        Intent intent = new Intent(getView().getActivity(), JujiaActivity.class);
        getView().startActivity(intent);
    }

    @OnClick(R.id.smarthome)
    void smartHome() {
        PluginEngineUtil.startSmartHome();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof NoticeEvent) {
                        NoticeEvent entity = (NoticeEvent) o;
                        getView().getNoticeContent().setText(StringUtil.delHTMLTag(entity.getContent()));
                        getView().getNoticeTitle().setText(TextUtils.isEmpty(entity.getTitle()) ? "" : entity.getTitle());

                        type = entity.getType();
                    } else if (o instanceof UserInfoEvent) {
                        Log.d("pluginUserInfo", "更新天气信息");
                        getNotices();
                    } else if (o instanceof NoticePushEvent) {
                        getNotices();
                    }
                });
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

//        getNotices(367, "42abcd66b653086cc5805902c1a2134c746fea39");

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof WeatherEvent) {
                        WeatherEvent event = (WeatherEvent) o;
                        WeatherEntity.WeatherDataEntity entity = event.getWeatherEntity();

                        SquareView restrictSv = getView().getFpRestrict();
                        SquareView temperatureSv = getView().getFpTemperature();

                        temperatureSv.showContentText(TextUtils.isEmpty(entity.getTemperature()) ? "0" : entity.getTemperature() + "°");
                        temperatureSv.showTitleText("pm2.5 " + (TextUtils.isEmpty(entity.getPm25()) ? "0" : entity.getPm25()));

                        String date = entity.getDate_one();
                        String restrictNo = getRestrictNo(date, entity.getRestrict());

                        if (TextUtils.isEmpty(restrictNo)) {
                            restrictSv.showContentText(DateUtil.getDate(date));
                            restrictSv.showContentTextSize(40);
                            restrictSv.showTitleText("不限行");
                        } else {
                            restrictSv.showContentText(restrictNo);
                            restrictSv.showContentTextSize(60);
                            restrictSv.showTitleText(DateUtil.getDate(date) + "限行");
                        }
                    }
                }));
    }

    private void getNotices() {
        addSubscription(model.getNotices()
                .subscribe(new Subscriber<NoticeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(NoticeEntity noticeEntity) {
                        if (!processNetworkResult(noticeEntity, false))
                            return;

                        if (noticeEntity == null ||
                                noticeEntity.getData() == null)
                            return;

                        getView().initNotices(noticeEntity.getData());
                    }
                }));
    }

    private String getRestrictNo(String date, WeatherEntity.Restrict restrict) {
        String restrictNo = "";

        if (TextUtils.isEmpty(date))
            return "";

        if (date.equals("周一")) {
            restrictNo = restrict.getMonday();
        } else if (date.equals("周二")) {
            restrictNo = restrict.getTuesday();
        } else if (date.equals("周三")) {
            restrictNo = restrict.getWednesday();
        } else if (date.equals("周四")) {
            restrictNo = restrict.getThursday();
        } else if (date.equals("周五")) {
            restrictNo = restrict.getFriday();
        } else if (date.equals("周六")) {
            restrictNo = restrict.getSaturday();
        } else if (date.equals("周日")) {
            restrictNo = restrict.getSunday();
        }

        return restrictNo;
    }
}
