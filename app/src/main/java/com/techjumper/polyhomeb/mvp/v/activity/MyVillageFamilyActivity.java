package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MyVillageFamilyActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MyVillageFamilyActivityPresenter.class)
public class MyVillageFamilyActivity extends AppBaseActivity<MyVillageFamilyActivityPresenter> {

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_my_village_family);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.may_village_or_family);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }
}
