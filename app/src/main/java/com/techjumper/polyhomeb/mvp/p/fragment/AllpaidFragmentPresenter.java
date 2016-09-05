package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.AllpaidFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.AllpaidFragment;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AllpaidFragmentPresenter extends AppBaseFragmentPresenter<AllpaidFragment> {

    public AllpaidFragmentModel mModel = new AllpaidFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
