package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.techjumper.commonres.PluginConstant;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.WeatherEntity;
import com.techjumper.commonres.entity.event.NoticeEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.WeatherEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
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

    @OnClick(R.id.property)
    void property() {
        PluginEngineUtil.startProperty();
    }

    @OnClick(R.id.notice_layout)
    void noticeLayout() {
        if (type == -1)
            return;

        PluginEngineUtil.startInfo(type);
    }

    @OnClick(R.id.ad)
    void ad() {
        Intent intent = new Intent(getView().getActivity(), AdActivity.class);
        getView().getActivity().startActivity(intent);
    }

    @OnClick(R.id.shopping)
    void shopping() {
        Intent intent = new Intent(getView().getActivity(), ShoppingActivity.class);
        getView().startActivity(intent);
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
                        getView().getNoticeTitle().setText(entity.getTitle());

                        type = entity.getType();
                    }
                });
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getNotices();

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof WeatherEvent) {
                        WeatherEvent event = (WeatherEvent) o;
                        WeatherEntity.WeatherDataEntity entity = event.getWeatherEntity();

                        SquareView restrictSv = getView().getFpRestrict();
                        SquareView temperatureSv = getView().getFpTemperature();

                        temperatureSv.showContentText(entity.getTemperature() + "°");
                        temperatureSv.showTitleText("pm" + entity.getPm25());

                        String date = entity.getDate_one();
                        String restrictNo = getRestrictNo(date, entity.getRestrict());

                        if (TextUtils.isEmpty(restrictNo)) {
                            restrictSv.showContentText("无");
                            restrictSv.showTitleText(DateUtil.getDate(date) + "不限行");
                        } else {
                            restrictSv.showContentText(restrictNo);
                            restrictSv.showTitleText(DateUtil.getDate(date) + "限行");
                        }

                    }
                }));
    }

    //
//    private void getNotice() {
//        addSubscription(model.getInfo(1)
//                .subscribe(new Subscriber<InfoEntity>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(InfoEntity infoEntity) {
//                        if (!processNetworkResult(infoEntity, false))
//                            return;
//
//                        if (infoEntity == null || infoEntity.getData() == null
//                                || infoEntity.getData().getMessages() == null)
//                            return;
//
//                        List<InfoEntity.InfoDataEntity.InfoItemEntity> infoItemEntities = infoEntity.getData().getMessages();
//                        if (infoItemEntities.size() > 0) {
//                            InfoEntity.InfoDataEntity.InfoItemEntity entity = infoItemEntities.get(0);
//                            getView().getNoticeTitle().setText(entity.getTitle());
//                            getView().getNoticeContent().setText(entity.getContent());
//                        } else {
//                            getView().getNoticeContent().setText("没有通知");
//                        }
//                    }
//                }));
//    }

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
