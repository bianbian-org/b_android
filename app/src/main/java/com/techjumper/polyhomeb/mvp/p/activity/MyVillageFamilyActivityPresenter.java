package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.mvp.v.activity.MyVillageFamilyActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MyVillageFamilyActivityPresenter extends AppBaseActivityPresenter<MyVillageFamilyActivity> {


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public void onTitleRightClick() {
        ToastUtils.show("跳转到ChooseXXX界面,并且带个参数,让那个界面知道需要显示返回键");
    }
}
