package com.techjumper.polyhome.b.home.mvp.p.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.techjumper.plugincommunicateengine.Constants;
import com.techjumper.plugincommunicateengine.HostDataBuilder;
import com.techjumper.plugincommunicateengine.PluginEngine;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.JujiaActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.ShoppingActivity;
import com.techjumper.polyhome.b.home.mvp.v.fragment.PloyhomeFragment;

import butterknife.OnClick;

/**
 * Created by kevin on 16/4/28.
 */
public class PloyhomeFragmentPresenter extends AppBaseFragmentPresenter<PloyhomeFragment> {

    @OnClick(R.id.property)
    void property() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName("com.techjumper.polyhome.b.property")
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    @OnClick(R.id.notice_layout)
    void noticeLayout() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName("com.techjumper.polyhome.b.info")
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
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
    void jujia(){
        Intent intent = new Intent(getView().getActivity(), JujiaActivity.class);
        getView().startActivity(intent);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
