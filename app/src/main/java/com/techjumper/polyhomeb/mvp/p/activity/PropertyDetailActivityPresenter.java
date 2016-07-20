package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.mvp.m.PropertyDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewComplainActivity;
import com.techjumper.polyhomeb.mvp.v.activity.NewRepairActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyDetailActivityPresenter extends AppBaseActivityPresenter<PropertyDetailActivity> {

    private PropertyDetailActivityModel mModel = new PropertyDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public int comeFromWitchButton() {
        return mModel.comeFromWitchButton();
    }

    public void onTitleRightClick() {

        switch (getView().getViewPager().getCurrentItem()) {
            case 1:
                new AcHelper.Builder(getView()).target(NewRepairActivity.class).start();
                break;
            case 2:
                new AcHelper.Builder(getView()).target(NewComplainActivity.class).start();
                break;
        }
    }
}
