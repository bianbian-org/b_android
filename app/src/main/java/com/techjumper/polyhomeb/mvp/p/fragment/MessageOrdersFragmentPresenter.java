package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.MessageOrdersFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageOrdersFragment;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageOrdersFragmentPresenter extends AppBaseFragmentPresenter<MessageOrdersFragment> {

    private MessageOrdersFragmentModel mModel = new MessageOrdersFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
