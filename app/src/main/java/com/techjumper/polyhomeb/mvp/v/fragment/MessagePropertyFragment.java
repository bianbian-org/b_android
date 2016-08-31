package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.mvp.p.fragment.MessagePropertyFragmentPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MessagePropertyFragmentPresenter.class)
public class MessagePropertyFragment extends AppBaseFragment<MessagePropertyFragmentPresenter> {

    public static MessagePropertyFragment getInstance() {
        return new MessagePropertyFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
