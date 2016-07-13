package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.fragment.FriendFragmentPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(FriendFragmentPresenter.class)
public class FriendFragment extends AppBaseFragment<FriendFragmentPresenter> {

    public static FriendFragment getInstance() {
        return new FriendFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
