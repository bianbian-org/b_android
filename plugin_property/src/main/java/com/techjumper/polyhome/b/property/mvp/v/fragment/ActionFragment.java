package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ActionFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(ActionFragmentPresenter.class)
public class ActionFragment extends AppBaseFragment<ActionFragmentPresenter> {


    public static ActionFragment getInstance() {
        return new ActionFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

}
