package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.fragment.RepairFragmentPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(RepairFragmentPresenter.class)
public class RepairFragment extends AppBaseFragment<RepairFragmentPresenter> {

    public static RepairFragment getInstance() {
        return new RepairFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repair, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
