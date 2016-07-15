package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.mvp.m.PlacardFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.PlacardFragment;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardFragmentPresenter extends AppBaseFragmentPresenter<PlacardFragment> {

    private PlacardFragmentModel mModel = new PlacardFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public List<DisplayBean> getData() {
        return mModel.initPlacardData();
    }
}
