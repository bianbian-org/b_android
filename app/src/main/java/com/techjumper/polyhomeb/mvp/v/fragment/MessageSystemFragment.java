package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.mvp.p.fragment.MessageSystemFragmentPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MessageSystemFragmentPresenter.class)
public class MessageSystemFragment extends AppBaseFragment<MessageSystemFragmentPresenter> {

    public static MessageSystemFragment getInstance() {
        return new MessageSystemFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
