package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.mvp.m.NewRepairActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewRepairActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewRepairActivityPresenter extends AppBaseActivityPresenter<NewRepairActivity> {

    private NewRepairActivityModel mModel = new NewRepairActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public void onTitleRightClick() {

        ToastUtils.show("提交");
    }

    //V调用
    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    //M调用
    public ArrayList<String> getChoosedPhoto() {
        return getView().getPhotos();
    }

}
