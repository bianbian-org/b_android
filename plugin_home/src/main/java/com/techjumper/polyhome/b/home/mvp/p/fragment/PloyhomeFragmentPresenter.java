package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.plugincommunicateengine.Constants;
import com.techjumper.plugincommunicateengine.HostDataBuilder;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.m.PloyhomeFragmentModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.JujiaActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.ShoppingActivity;
import com.techjumper.polyhome.b.home.mvp.v.fragment.PloyhomeFragment;

import java.util.List;

import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by kevin on 16/4/28.
 */
public class PloyhomeFragmentPresenter extends AppBaseFragmentPresenter<PloyhomeFragment> {

    private PloyhomeFragmentModel model = new PloyhomeFragmentModel(this);

    @OnClick(R.id.property)
    void property() {
        PluginEngineUtil.startProperty();
    }

    @OnClick(R.id.notice_layout)
    void noticeLayout() {
        PluginEngineUtil.startInfo();
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

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getNotice();
    }

    private void getNotice() {
        addSubscription(model.getInfo(1)
                .subscribe(new Subscriber<InfoEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InfoEntity infoEntity) {
                        if (!processNetworkResult(infoEntity, false))
                            return;

                        if (infoEntity == null || infoEntity.getData() == null
                                || infoEntity.getData().getMessages() == null)
                            return;

                        List<InfoEntity.InfoDataEntity.InfoItemEntity> infoItemEntities = infoEntity.getData().getMessages();
                        if (infoItemEntities.size() > 0) {
                            InfoEntity.InfoDataEntity.InfoItemEntity entity = infoItemEntities.get(0);
                            getView().getNoticeTitle().setText(entity.getTitle());
                            getView().getNoticeContent().setText(entity.getContent());
                        } else {
                            getView().getNoticeContent().setText("没有通知");
                        }
                    }
                }));
    }
}
