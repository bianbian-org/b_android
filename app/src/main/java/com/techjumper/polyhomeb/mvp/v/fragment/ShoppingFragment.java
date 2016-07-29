package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.fragment.ShoppingFragmentPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ShoppingFragmentPresenter.class)
public class ShoppingFragment extends AppBaseFragment<ShoppingFragmentPresenter> {

    public static ShoppingFragment getInstance() {
        return new ShoppingFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public String getTitle() {
        return "商城";
    }
}
