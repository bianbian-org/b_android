package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.mvp.m.RepairFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.RepairFragment;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RepairFragmentPresenter extends AppBaseFragmentPresenter<RepairFragment> {

    private RepairFragmentModel mModel = new RepairFragmentModel(this);
    private boolean mIsFirst = true;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (mIsFirst && isVisibleToUser) {
            mIsFirst = false;
            List<DisplayBean> mDatas = mModel.initPlacardData();
            getView().show(mDatas);
        }
    }
}
