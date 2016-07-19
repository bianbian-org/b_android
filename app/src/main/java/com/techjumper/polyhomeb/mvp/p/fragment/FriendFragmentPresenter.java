package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.v.fragment.FriendFragment;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class FriendFragmentPresenter extends AppBaseFragmentPresenter<FriendFragment> {

    private boolean mIsFirst = true;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (mIsFirst && isVisibleToUser) {
//            mIsFirst = false;
//            List<DisplayBean> datas = mModel.initPropertyData();
//            getView().show(datas);
//        }
    }
}
