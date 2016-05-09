package com.techjumper.polyhome.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.mvp.p.fragment.LoginFragmentPresenter;

/**
 * Created by kevin on 16/5/6.
 */
@Presenter(LoginFragmentPresenter.class)
public class LoginFragment extends AppBaseFragment<LoginFragmentPresenter> {

    public static LoginFragment getInstance(){
        return new LoginFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_login, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
