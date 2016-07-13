package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.fragment.PlacardFragmentPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(PlacardFragmentPresenter.class)
public class PlacardFragment extends AppBaseFragment<PlacardFragmentPresenter> {

    public static PlacardFragment getInstance() {
        return new PlacardFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_placard, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
