package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.mvp.p.fragment.ServiceFragmentPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ServiceFragmentPresenter.class)
public class ServiceFragment extends AppBaseFragment<ServiceFragmentPresenter> {

    public static ServiceFragment getInstance() {
        return new ServiceFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
