package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.MessageFriendFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageFriendFragment;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageFriendFragmentPresenter extends AppBaseFragmentPresenter<MessageFriendFragment> {

    private MessageFriendFragmentModel mModel = new MessageFriendFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
