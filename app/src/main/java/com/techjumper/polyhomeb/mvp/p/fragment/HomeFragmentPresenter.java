package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.mvp.m.HomeFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeFragment;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeFragmentPresenter extends AppBaseFragmentPresenter<HomeFragment> {

    private HomeFragmentModel mModel = new HomeFragmentModel(this);
    private boolean mIsFirst = true;
    private List<DisplayBean> mDatas;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (mIsFirst && isVisibleToUser) {
//            mIsFirst = false;
//            mDatas = mModel.initPropertyData();
//        }
//    }

    public List<DisplayBean> getDatas() {
        return mModel.initPropertyData();
    }
}
