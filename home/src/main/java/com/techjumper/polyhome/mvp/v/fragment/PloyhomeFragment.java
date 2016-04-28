package com.techjumper.polyhome.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.mvp.p.fragment.PloyhomeFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(PloyhomeFragmentPresenter.class)
public class PloyhomeFragment extends AppBaseFragment<PloyhomeFragmentPresenter> {

    public static PloyhomeFragment getInstance() {
        return new PloyhomeFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ployhome, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
