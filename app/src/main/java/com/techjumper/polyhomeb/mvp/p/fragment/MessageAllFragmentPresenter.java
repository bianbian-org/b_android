package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.MessageAllFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageAllFragment;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageAllFragmentPresenter extends AppBaseFragmentPresenter<MessageAllFragment> {

    private MessageAllFragmentModel mModel = new MessageAllFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
