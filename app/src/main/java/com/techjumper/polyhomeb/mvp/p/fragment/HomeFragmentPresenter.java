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

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public List<DisplayBean> getData() {
        return mModel.initPropertyData();
    }
}
