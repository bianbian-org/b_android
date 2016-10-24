package com.techjumper.polyhome.b.property.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.property.mvp.p.fragment.PropertyFragmentPresenter;

/**
 * Created by kevin on 16/10/24.
 */
@Presenter(PropertyFragmentPresenter.class)
public class PropertyFragment extends AppBaseFragment<PropertyFragmentPresenter> {
    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
