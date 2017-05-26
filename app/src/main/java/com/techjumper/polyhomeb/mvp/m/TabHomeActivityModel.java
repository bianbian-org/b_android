package com.techjumper.polyhomeb.mvp.m;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.UpdateInfoEntity;
import com.techjumper.polyhomeb.mvp.p.activity.TabHomeActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class TabHomeActivityModel extends BaseModel<TabHomeActivityPresenter> {

    public TabHomeActivityModel(TabHomeActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<UpdateInfoEntity> checkUpdate(String[] packageInfos) {
        KeyValuePair keyValuePair = KeyValueCreator.getAppUpdateInfo(1,packageInfos);
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getAppUpdateInfo(map)
                .compose(CommonWrap.wrap());
    }

    public int getVersionCode() {
        int versionCode = -1;
        try {
            PackageManager manager = getPresenter().getView().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPresenter().getView().getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void analyzeVersionCode(List<UpdateInfoEntity.DataBean.ResultBean> result) {
        for (int i = 0; i < result.size(); i++) {
            UpdateInfoEntity.DataBean.ResultBean resultBean = result.get(i);
            String version = resultBean.getVersion();
            int serverVersion = NumberUtil.convertToint(version, -1);
            if (serverVersion > getVersionCode()) {
                getPresenter().showDialog(resultBean.getUrl());
                break;
            }
        }
    }
}
