package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.mvp.m.ComplainFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.ComplainFragment;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ComplainFragmentPresenter extends AppBaseFragmentPresenter<ComplainFragment> {

    private ComplainFragmentModel mModel = new ComplainFragmentModel(this);

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
