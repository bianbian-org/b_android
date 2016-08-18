package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.mvp.v.activity.NewInvitationActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewInvitationActivityPresenter extends AppBaseActivityPresenter<NewInvitationActivity> {
    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public void onTitleRightClick(){
        ToastUtils.show("提交");
    }
}
